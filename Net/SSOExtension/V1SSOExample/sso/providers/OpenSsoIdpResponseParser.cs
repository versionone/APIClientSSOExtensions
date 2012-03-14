using System.IO;
using HtmlAgilityPack;
using VersionOne.SDK.APIClient.SSOExtension;

namespace V1SSOExample.sso.providers
{
    public class OpenSsoIdpResponseParser : IResponseParser
    {
        private readonly HtmlDocument _doc = new HtmlDocument();

        public string PostUrl { get { return UrlAuthority + GetValueFromNode("//form[@name='Login']", "action"); } }
        public string UrlAuthority { get; set; }
        public void LoadResponse(Stream stream) { _doc.Load(stream); }


        private string GetValueFromNode(string elementXpath, string attributeName)
        {
            HtmlNode node = _doc.DocumentNode.SelectSingleNode(elementXpath);
            if ((node != null) && (node.Attributes != null))
            {
                return node.Attributes[attributeName].Value;
            }
            return null;
        }

        private string SamlResponse { get { return GetValueFromNode("//input[@name=\"goto\"]", "value"); } }
        private string RelayState { get { return GetValueFromNode("//input[@name=\"SunQueryParamsString\"]", "value"); } }

        public PostData PostData
        {
            get
            {
                var formData = new IdpPostData();
                formData.Add("IDButton");
                formData.Add("goto", SamlResponse);
                formData.Add("SunQueryParamsString", RelayState);
                formData.Add("encoded", "true");
                formData.Add("gx_charset", "UTF-8");
                formData.Add("gx_charset", "UTF-8");
                return formData;
            }
        }

        internal class IdpPostData : PostData
        {
            public override void SetCredentials(string username, string password)
            {
                Add("IDToken1", username);
                Add("IDToken2", password);
            }
        }
    }
}
