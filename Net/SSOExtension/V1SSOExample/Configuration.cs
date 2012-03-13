
using System.Configuration;
using VersionOne.SDK.APIClient.SSOExtension;

namespace V1SSOExample
{
    class Configuration : IVersionOneSsoConfiguration
    {
        private static Configuration _impl;

        public static Configuration Instance()
        {
            return _impl ?? (_impl = new Configuration());
        }

        public string ServerUrl
        {
            get { return ConfigurationManager.AppSettings["v1ServerUrl"]; }
        }

        public string Username
        {
            get { return ConfigurationManager.AppSettings["username"]; }
        }

        public string Password
        {
            get { return ConfigurationManager.AppSettings["password"]; }
        }

        public bool IntegratedAuth
        {
            get { return bool.Parse(ConfigurationManager.AppSettings["integrated"]); }
        }

        public string IdpUrl
        {
            get { return ConfigurationManager.AppSettings["idpURL"]; }
        }

        public string IdpResponseParser
        {
            get { return ConfigurationManager.AppSettings["idpResponseParser"]; }
        }

        public string SpResponseParser
        {
            get { return ConfigurationManager.AppSettings["spResponseParser"]; }
        }
    }
}
