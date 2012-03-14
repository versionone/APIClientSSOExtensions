using System;

namespace VersionOne.SDK.APIClient.SSOExtension
{
    /// <summary>
    /// Factory for creating response parsers
    /// </summary>
    internal class ResponseParserFactory
    {
        private string IdpParserClassType { get; set; }
        private string SpParserClassType { get; set; }

        /// <summary>
        /// Construction
        /// </summary>
        /// <param name="config"></param>
        public ResponseParserFactory(IVersionOneSsoConfiguration config)
        {
            IdpParserClassType = config.IdpResponseParser;
            SpParserClassType = config.SpResponseParser;
        }

        /// <summary>
        /// Create the Identity Provider parser
        /// </summary>
        /// <returns></returns>
        public IResponseParser CreateIdentityProviderResponseParser()
        {
            var type = Type.GetType(IdpParserClassType, true);
            var parser = Activator.CreateInstance(type) as IResponseParser;
            if (null == parser)
                throw new V1Exception("Cannot create Identity Provider Response Parser");
            return parser;
        }

        /// <summary>
        /// Create the Service Provider parser
        /// </summary>
        /// <returns></returns>
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
