using System;

namespace VersionOne.SDK.APIClient.SSOExtension
{
    internal class ResponseParserFactory
    {
        private string IdpParserClassType { get; set; }
        private string SpParserClassType { get; set; }

        public ResponseParserFactory(IVersionOneSsoConfiguration config)
        {
            IdpParserClassType = config.IdpResponseParser;
            SpParserClassType = config.SpResponseParser;
        }

        public IResponseParser CreateIdentityProviderResponseParser()
        {
            var type = Type.GetType(IdpParserClassType, true);
            var parser = Activator.CreateInstance(type) as IResponseParser;
            if (null == parser)
                throw new V1Exception("Cannot create Identity Provider Response Parser");
            return parser;
        }

        public IResponseParser CreateServiceProviderResponseParser()
        {
            var type = Type.GetType(SpParserClassType, true);
            var parser = Activator.CreateInstance(type) as IResponseParser;
            if (null == parser)
                throw new V1Exception("Cannot create Service Provider Respone Parser");
            return parser;
        }
    }
}
