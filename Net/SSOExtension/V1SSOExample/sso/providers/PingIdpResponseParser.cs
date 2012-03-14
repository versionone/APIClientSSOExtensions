using System.IO;
using System.Xml;
using VersionOne.SDK.APIClient.SSOExtension;

namespace V1SSOExample.sso.providers
{
    public class PingIdpResponseParser :  IResponseParser
    {
        private readonly XmlDocument _doc = new XmlDocument();

        public string PostUrl { get { return GetValueFromNode("//form", "action"); } }
        public string UrlAuthority { get; set; }
        public void LoadResponse(Stream stream) { _doc.Load(stream); }

        private string GetValueFromNode(string elementXpath, string attributeName)
        {
            XmlNode node = _doc.SelectSingleNode(elementXpath);
            if ((node != null) && (node.Attributes != null))
            {
                return node.Attributes[attributeName].Value;
            }
            return null;
        }

        private string SamlResponse { get { return GetValueFromNode("//input[@name=\"SAMLResponse\"]", "value"); } }
        private string RelayState { get { return GetValueFromNode("//input[@name=\"RelayState\"]", "value"); } }

        public PostData PostData
        {
            get 
            { 
                var formData = new PingPostData();
                formData.Add("SAMLResponse", SamlResponse);
                formData.Add("RelayState", RelayState);
                return formData;
            }
        }
    }

    internal class PingPostData : PostData
    {
        public override void SetCredentials(string username, string password)
        {
            // noting to do here
        }
    }
}
