using System.IO;
using System.Web;
using HtmlAgilityPack;
using VersionOne.SDK.APIClient.SSOExtension;

namespace V1SSOExample.sso.providers
{
    public class OpenSsoSpResponseParser : IResponseParser
    {
        private readonly HtmlDocument _doc = new HtmlDocument();

        public string PostUrl { get { return GetValueFromNode("//form", "action"); } }
        public string SamlResponse { get { return GetValueFromNode("//input[@name=\"SAMLResponse\"]", "value"); } }
        public string RelayState { get { return GetValueFromNode("//input[@name=\"RelayState\"]", "value"); } }
        public string UrlAuthority { get; set; }
        public void Load(Stream stream) { _doc.Load(stream); }

        private string GetValueFromNode(string elementXpath, string attributeName)
        {
            HtmlNode node = _doc.DocumentNode.SelectSingleNode(elementXpath);
            if ((node != null) && (node.Attributes != null))
            {
                return node.Attributes[attributeName].Value;
            }
            return null;
        }

        public FormData FormData
        {
            get 
            { 
                var formData = new SpFormData();
                formData.Add("SAMLResponse", HttpUtility.HtmlDecode(SamlResponse));
                formData.Add("RelayState", HttpUtility.HtmlDecode(RelayState));
                return formData;
            }
        }
    }

    internal class SpFormData : FormData
    {
        public override void SetCredentials(string username, string password)
        {
            // does nothing because there are no credentials on this page.
        }
    }
}
