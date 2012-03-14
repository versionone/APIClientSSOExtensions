using System.IO;

namespace VersionOne.SDK.APIClient.SSOExtension
{
    /// <summary>
    /// Interface that a SSO provider (IDP and SP) needs to implement
    /// </summary>
    public interface IResponseParser
    {
        /// <summary>
        /// The URL 
        /// </summary>
        string PostUrl { get; }

        /// <summary>
        /// This is the UriPartial.Authority from the server that sent us a response.  
        /// This is important when the PostsUrl is relative.
        /// </summary>
        string UrlAuthority { set; }

        /// <summary>
        /// Load the response received from a server
        /// </summary>
        /// <param name="stream"></param>
        void LoadResponse(Stream stream);

        /// <summary>
        /// Return the post data necessary to take the next step.
        /// </summary>
        /// <returns></returns>
        PostData PostData { get; }
    }
}
