namespace VersionOne.SDK.APIClient.SSOExtension
{
    /// <summary>
    /// The configuration requirements for SSO connectivity
    /// </summary>
    public interface IVersionOneSsoConfiguration
    {
        /// <summary>
        /// URL to VersionOne instance.  Must end with a slash (/)
        /// </summary>
        string ServerUrl { get; }

        /// <summary>
        /// Valid user for VersionOne instance
        /// In SSO, if your IDP uses integrated auth, you may not need to provide this information
        /// </summary>
        string Username { get; }

        /// <summary>
        /// Password for user
        /// In SSO, if your IDP uses integrated auth, you may not need to provide this information
        /// </summary>
        string Password { get; }

        /// <summary>
        /// Indicates if the connection should use integrated authentication
        /// </summary>
        bool IntegratedAuth { get; }

        /// <summary>
        /// URL to SSO Identity Provider (IDP)
        /// </summary>
        string IdpUrl { get; }

        /// <summary>
        /// Class that will parse IDP response
        /// </summary>
        string IdpResponseParser { get; }

        /// <summary>
        /// Class that will parse SP response
        /// </summary>
        string SpResponseParser { get; }

        /// <summary>
        /// URI to be used for Authentication. This parameter is required if the integrated parameter above is set to true.
        /// </summary>
        string AuthenticationUri { get; }

    }
}
