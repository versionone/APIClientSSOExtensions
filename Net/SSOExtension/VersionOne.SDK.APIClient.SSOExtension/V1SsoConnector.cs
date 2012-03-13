using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Web;

namespace VersionOne.SDK.APIClient.SSOExtension
{
    public class V1SsoConnector : IAPIConnector
    {
        private readonly string _v1Url;
        private readonly string _idpUrl;

        private readonly string _username;
        private readonly string _password;
        private readonly bool _integratedauth;

        private readonly ResponseParserFactory _responseParserFactory;

        public V1SsoConnector(IVersionOneSsoConfiguration config, string pathExtension)
        {
            _v1Url = config.ServerUrl + pathExtension;
            _idpUrl = config.IdpUrl;
            _username = config.Username;
            _password = config.Password;
            _integratedauth = config.IntegratedAuth;
            _responseParserFactory = new ResponseParserFactory(config);
        }

        public void Authenticate()
        {
            IResponseParser idpResponse = IdentityProviderRequest();
            IResponseParser spResponse = IdentityProviderAuthentication(idpResponse);
            ServiceProviderRequest(spResponse);
        }

        public Stream GetData()
        {
            return GetData(string.Empty);
        }

        public Stream GetData(string path)
        {
            HttpWebRequest request = CreateRequest(_v1Url + path);
            WebResponse response = request.GetResponse();
            if (((HttpWebResponse)response).StatusCode == HttpStatusCode.Redirect)
            {
                // ToDo: Need some retry logic here so we don't get caught in a loop
                Authenticate();
                return GetData(path);
            }

            #region Code used when debugging
            //Console.WriteLine("");
            //Console.WriteLine("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            //Console.WriteLine("Get....");
            //Console.WriteLine("URL: " + url);
            //Console.WriteLine("Response from: " + response.ResponseUri);
            //Console.WriteLine("Status: " + response.StatusCode);
            //Console.WriteLine("---------------------------------------------------------------------------------------------------");
            //Console.WriteLine(response.Headers.ToString());
            //Console.WriteLine("---------------------------------------------------------------------------------------------------");
            //Console.WriteLine(response.ToString());
            //Console.WriteLine("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");))
            #endregion
            
            return response.GetResponseStream();
        }

        public Stream SendData(string path, string data)
        {
            HttpWebRequest request = CreateRequest(_v1Url + path);
            request.ServicePoint.Expect100Continue = false;

            request.Method = "POST";
            request.ContentType = "type/xml";

            #region Code used when debugging
            //Console.WriteLine("");
            //Console.WriteLine("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            //Console.WriteLine("POST....");
            //Console.WriteLine("URL: " + _v1Url + path);
            //Console.WriteLine(request.Headers.ToString());
            //Console.WriteLine(data);
            //Console.WriteLine("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            //Console.WriteLine("");
            #endregion

            using (var stream = new StreamWriter(request.GetRequestStream()))
                stream.Write(data);

            return request.GetResponse().GetResponseStream();

        }

        private readonly IDictionary<string, HttpWebRequest> _requests = new Dictionary<string, HttpWebRequest>();
        public Stream BeginRequest(string path)
        {
            HttpWebRequest req = CreateRequest(_v1Url + path);
            _requests[path] = req;
            req.Method = "POST";
            return req.GetRequestStream();
        }

        public Stream EndRequest(string path, string contentType)
        {
            HttpWebRequest req = _requests[path];
            _requests.Remove(path);
            req.ContentType = contentType;
            return req.GetResponse().GetResponseStream();
        }

        private readonly IDictionary<string, string> _customHttpHeaders = new Dictionary<string, string>();
        public IDictionary<string, string> CustomHttpHeaders
        {
            get { return _customHttpHeaders; }
        }

        private HttpWebRequest CreateRequest(string path)
        {
            var request = (HttpWebRequest)WebRequest.Create(path);

            request.Headers.Add("Accept-Language", System.Globalization.CultureInfo.CurrentCulture.Name);

            foreach (KeyValuePair<string, string> pair in _customHttpHeaders)
            {
                request.Headers.Add(pair.Key, pair.Value);
            }
            request.CookieContainer = CookieJar;
            request.Credentials = Credentials;
            request.UnsafeAuthenticatedConnectionSharing = true;
            request.AllowAutoRedirect = false;
            return request;
        }

        #region CredentialCache
        private CredentialCache _credentials;
        private CredentialCache Credentials
        {
            get
            {
                if (_credentials == null)
                {
                    _credentials = new CredentialCache();
                    var uri = new Uri(new Uri(_idpUrl).GetLeftPart(UriPartial.Path));

                    if (!string.IsNullOrEmpty(_username) || !string.IsNullOrEmpty(_password))
                    {
                        var credential = new NetworkCredential(_username, _password);
                        if (_integratedauth)
                        {
                            _credentials.Add(uri, "Negotiate", credential);
                            _credentials.Add(uri, "NTLM", credential);
                        }
                        else
                            _credentials.Add(uri, "Basic", credential);
                    }
                    else
                    {
                        var credential = CredentialCache.DefaultCredentials as NetworkCredential;
                        if (credential != null)
                        {
                            _credentials.Add(uri, "Negotiate", credential);
                            _credentials.Add(uri, "NTLM", credential);
                        }
                    }
                }
                return _credentials;
            }
        }
        #endregion

        #region CookieContainer
        private CookieContainer _cookieJar;
        private CookieContainer CookieJar
        {
            get { return _cookieJar ?? (_cookieJar = new CookieContainer()); }
        }
        #endregion

        #region Authentication Private Methods
        /// <summary>
        /// Make a request to the IdP to get the Response value.
        /// 
        /// ** This is here, as opposed to a seperate object because we need to share the Cookie container **
        /// 
        /// </summary>
        /// <returns></returns>
        private IResponseParser IdentityProviderRequest()
        {
            HttpWebRequest idpRequest = CreateRequest(_idpUrl);
            idpRequest.AllowAutoRedirect = true;

            var responseParser = _responseParserFactory.CreateIdentityProviderResponseParser();

            using (WebResponse idpResponse = idpRequest.GetResponse())
            {
                using (Stream stream = idpResponse.GetResponseStream())
                    responseParser.Load(stream);

                responseParser.UrlAuthority = idpResponse.ResponseUri.GetLeftPart(UriPartial.Authority);
            }
            return responseParser;
        }

        /// <summary>
        /// Post the response from the Identity Provider challenge to the proper URL for authentication
        /// 
        /// ** This is here, as opposed to a seperate object because we need to share the Cookie container **
        /// 
        /// </summary>
        /// <param name="response"></param>
        private IResponseParser IdentityProviderAuthentication(IResponseParser response)
        {
            var formData = response.FormData;
            formData.SetCredentials(_username, _password);

            var postData = formData.ToPostData();

            // Prepare web request...
            HttpWebRequest request = CreateRequest(response.PostUrl);
            request.AllowAutoRedirect = true;
            request.ServicePoint.Expect100Continue = false;
            request.Method = "POST";
            request.ContentType = "application/x-www-form-urlencoded";
            request.ContentLength = postData.Length;
            using (var stream = new StreamWriter(request.GetRequestStream()))
            {
                stream.Write(postData);
            }

            var responseParser = _responseParserFactory.CreateServiceProviderResponseParser();
            using (WebResponse idpResponse = request.GetResponse())
            {
                using (Stream responseStream = idpResponse.GetResponseStream())
                    responseParser.Load(responseStream);
            }
            return responseParser;
        }

        private void ServiceProviderRequest(IResponseParser response)
        {
            var formData = response.FormData;
            var postData = formData.ToPostData();

            // Prepare web request...
            string url = HttpUtility.HtmlDecode(response.PostUrl);
            HttpWebRequest request = CreateRequest(url);
            request.AllowAutoRedirect = true;
            request.ServicePoint.Expect100Continue = false;
            request.Method = "POST";
            request.ContentType = "application/x-www-form-urlencoded";
            request.ContentLength = postData.Length;

            using (var stream = new StreamWriter(request.GetRequestStream()))
            {
                stream.Write(postData);
            }
            request.GetResponse().Close();
        }

        #endregion

    }
}
