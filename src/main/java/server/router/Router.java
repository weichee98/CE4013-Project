package main.java.server.router;

import main.java.server.service.BankServices;
import main.java.shared.request.*;
import main.java.shared.response.*;
import main.java.shared.udp.UDPMessage;

import java.net.SocketAddress;
import java.util.UUID;

public class Router {
    private final BankServices bs;

    public Router(BankServices bs) {
        this.bs = bs;
    }

    public UDPMessage route(UDPMessage rawReqMessage) {
        SocketAddress address = rawReqMessage.getAddress();
        Request req = Request.fromBytes(rawReqMessage.getBytes());
        RequestHeader reqHeader = req.getHeader();
        byte[] reqBodyBytes = req.getReqBodyBytes();

        RequestBody reqBody;
        ResponseType respType;
        ResponseBody respBody;

        switch (reqHeader.getRequestType()) {
            case OPEN_ACCOUNT:
                reqBody = OpenAccountRequest.fromBytes(reqBodyBytes);
                respType = ResponseType.OPEN_ACCOUNT;
                respBody = bs.openAccount((OpenAccountRequest) reqBody);
                break;
            case CLOSE_ACCOUNT:
                reqBody = CloseAccountRequest.fromBytes(reqBodyBytes);
                respType = ResponseType.CLOSE_ACCOUNT;
                respBody = bs.closeAccount((CloseAccountRequest) reqBody);
                break;
            case SUBSCRIBE:
                reqBody = SubscribeRequest.fromBytes(reqBodyBytes);
                respType = ResponseType.SUBSCRIBE_STATUS;
                respBody = bs.requestSubscription(address, (SubscribeRequest) reqBody);
                break;
            case DEPOSIT:
                reqBody = DepositAndWithdrawRequest.fromBytes(reqBodyBytes);
                respType = ResponseType.DEPOSIT;
                respBody = bs.depositAndWithdraw((DepositAndWithdrawRequest) reqBody);
                break;
            case WITHDRAW:
                reqBody = DepositAndWithdrawRequest.fromBytes(reqBodyBytes);
                respType = ResponseType.WITHDRAW;
                respBody = bs.depositAndWithdraw((DepositAndWithdrawRequest) reqBody);
                break;
            case TRANSFER:
                reqBody = TransferRequest.fromBytes(reqBodyBytes);
                respType = ResponseType.TRANSFER;
                respBody = bs.transfer((TransferRequest) reqBody);
                break;
            case QUERY_ACCOUNT:
                reqBody = QueryAccountRequest.fromBytes(reqBodyBytes);
                respType = ResponseType.QUERY_ACCOUNT;
                respBody = bs.queryAccount((QueryAccountRequest) reqBody);
                break;
            default:
                respType = ResponseType.INVALID;
                respBody = new InvalidResponse(
                        "Unexpected request type: " + reqHeader.getRequestType()
                );
                break;
        }

        byte[] respBodyBytes = respBody.toBytes();
        ResponseHeader respHeader = new ResponseHeader(reqHeader.getUUID(), respType, respBodyBytes.length);
        Response resp = new Response(respHeader, respBodyBytes);
        return new UDPMessage(address, resp.toBytes());
    }
}
