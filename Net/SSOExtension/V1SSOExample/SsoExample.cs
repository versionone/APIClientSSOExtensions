using System;
using VersionOne.SDK.APIClient;
using VersionOne.SDK.APIClient.SSOExtension;

namespace V1SSOExample
{
    /// <summary>
    /// Demonstrate SSO connector
    /// </summary>
    internal class SsoExample : ISdkSample
    {
        public IMetaModel Meta { get; private set; }
        public IServices Services { get; private set; }

        public void CommandLineArguments(string[] args) {}
        public void LoadConfig() {}

        public void Connect()
        {
            Log("Create Meta Connector");
            var metaConnector = V1SsoConnector.CreateMetaConnection(Configuration.Instance());

            Log("Created Meta Model");
            Meta = new MetaModel(metaConnector);

            Log("Creating Data Connector");
            var dataConnector = V1SsoConnector.CreateDataConnection(Configuration.Instance(), false); 

            Log("Authenticate");
            dataConnector.Authenticate();

            Log("Created Services");
            Services = new Services(Meta, dataConnector);
        }

        public void Run()
        {
            Log("Get some meta stuff");
            IAssetType memberType = Meta.GetAssetType("Member");
            Log(memberType.DisplayName);

            Log("Perform a query");
            var query = new Query(memberType);
            QueryResult result = Services.Retrieve(query);

            Log(string.Format("Server returned {0} members", result.TotalAvaliable));
        }

        private static void Log(string message)
        {
            var now = DateTime.Now;
            Console.WriteLine("{0} {1} - {2}", now.ToShortDateString(), now.ToLongTimeString(), message);
        }

    }
}