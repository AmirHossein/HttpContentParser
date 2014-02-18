/**
 * User: amirhossein
 * Date: 2014/02/18 - 11:30
 */
public class HttpResponseParser extends HttpRequestParser {
    private int statusCode;
    private String statusMessage;

    public HttpResponseParser(String request) {
        super(request);
    }

    @Override
    protected void parseIdentifier(String[] identifiers) {
        this.version = identifiers[0];
        this.statusCode = Integer.parseInt(identifiers[1]);
        this.statusMessage = identifiers[2];
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
