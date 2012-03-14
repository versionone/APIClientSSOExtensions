using System;
using System.Collections;
using System.Web;

namespace VersionOne.SDK.APIClient.SSOExtension
{
    /// <summary>
    /// Data posted to SSO Providers (IDP and SP)
    /// This object needs to be created and returned by the class that implements IResponseProvider
    /// </summary>
    public abstract class PostData
    {
        private readonly ArrayList _formData = new ArrayList();

        /// <summary>
        /// Add a key-value pair
        /// results in post data that read key=value
        /// </summary>
        /// <param name="key"></param>
        /// <param name="value"></param>
        public void Add(string key, string value)
        {
            _formData.Add(string.Format("{0}={1}", key, HttpUtility.UrlEncode(value)));
        }

        /// <summary>
        /// adds a key with no value
        /// results in post data that reads key=
        /// </summary>
        /// <param name="key"></param>
        public void Add(string key)
        {
            _formData.Add(string.Format("{0}=", key));
        }

        /// <summary>
        /// Returns the post data string
        /// </summary>
        /// <returns></returns>
        public override string ToString()
        {
            return String.Join("&", (String[])_formData.ToArray(typeof(string)));
        }

        /// <summary>
        /// Identity Providers have different field names for credentials.   
        /// This abstraction allows the code to make one call to set credentials. 
        /// </summary>
        /// <param name="username"></param>
        /// <param name="password"></param>
        public abstract void SetCredentials(string username, string password);
    }
}