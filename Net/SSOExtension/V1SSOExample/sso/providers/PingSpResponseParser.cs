using System.IO;
using System.Xml;
using VersionOne.SDK.APIClient.SSOExtension;

namespace V1SSOExample.sso.providers
{
    public class PingSpResponseParser : IResponseParser
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

        private string OpenToken { get { return GetValueFromNode("//input[@name=\"opentoken\"]", "value"); } }

        public PostData PostData
        {
            get
            {
                var formData = new PingPostData();
                formData.Add("opentoken", OpenToken);
                return formData;
            }
        }
    }
}
