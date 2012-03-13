using System;
using VersionOne.SDK.APIClient;
using VersionOne.SDK.APIClient.SSOExtension;

namespace V1SSOExample
{
    internal class SsoExample : ISdkSampleBase
    {
        public IMetaModel Meta { get; private set; }
        public IServices Services { get; private set; }

        public void CommandLineArguments(string[] args) {}
        public void LoadConfig() {}

        public void Connect()
        {
            Log("Create Meta Connector");
            var metaConnector = new V1SsoConnector(Configuration.Instance(), "/meta.v1/");
            Log("Created Meta Model");
            Meta = new MetaModel(metaConnector);

            Log("Creating Data Connector");
            var dataConnector = new V1SsoConnector(Configuration.Instance(), "/rest-1.v1/");
            Log("Authenticate");
            dataConnector.Authenticate();
            Services = new Services(Meta, dataConnector);
        }

        public void Run()
        {
            Log("Get some meta stuff");
            IAssetType memberType = Meta.GetAssetType("Member");
            Log(memberType.DisplayName);

            Log("Go");
            var query = new Query(memberType);
            QueryResult result = Services.Retrieve(query);
            Console.WriteLine("Server returned {0} members", result.TotalAvaliable);
        }

        private static void Log(string message)
        {
            DateTime now = DateTime.Now;
            Console.WriteLine("{0} {1} - {2}", now.ToShortDateString(), now.ToLongTimeString(), message);
        }

    }
}