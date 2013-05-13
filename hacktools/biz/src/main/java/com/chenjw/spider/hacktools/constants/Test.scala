import scala.actors.Actor, java.util._  
  
abstract class AuctionMessage  
case class Offer(bid: Int, client: Actor) extends AuctionMessage  
case class Inquire(client: Actor) extends AuctionMessage  
case object TIMEOUT  
  
abstract class AuctionReply  
case class Status(asked: Int, expire: Date) extends AuctionReply  
case object BestOffer extends AuctionReply  
case class BeatenOffer(maxBid: Int) extends AuctionReply  
case class AuctionConcluded(seller: Actor, client: Actor) extends AuctionReply  
case object AuctionFailed extends AuctionReply  
case object AuctionOver extends AuctionReply  
  
/** 
* Before finally stopping, it stays active for another period determined by the 
* timeToShutdown constant and replies to further offers that the auction is closed 
**/  
class Auction (seller: Actor, minBid: Int, closing: Date) extends Actor{  
    // 60 minutes to shut down the auction  
    val timeToShutdown = 3600000  
    // minimum bid for each offer  
    val bidIncrement = 10  
      
    def act(){  
        var maxBid = minBid - bidIncrement  
        var maxBidder: Actor = null  
        var running = true  
          
        while(running){  
            //receiveWithin: just span a period of time for mailbox messages then stopped  
            receiveWithin ((closing.getTime() - new Date().getTime())){  
                case Offer(bid, client) =>  
                    if(bid >= maxBid + bidIncrement){  
                        //beat successfully, notify the current maxBidder, then replace it  
                        if(maxBid >= minBid) maxBidder ! BeatenOffer(bid)  
                        //reply to client the current offer peak value  
                        maxBid = bid;   maxBidder = client; client ! BestOffer  
                    }else{  
                        //beat failed, return the current max bid value to offer client  
                        client ! BeatenOffer(maxBid)  
                    }  
                case Inquire(client) =>  
                    // query the max bid and closing time  
                    client ! Status(maxBid, closing)  
                case TIMEOUT =>  
                    //auction done  
                    if(maxBid >= minBid){  
                        val reply = AuctionConcluded(seller, maxBidder)  
                        maxBidder ! reply; seller ! reply  
                    }else{  
                        //no one get the auction, notify seller  
                        seller ! AuctionFailed  
                    }  
                      
                    //for further offer message, tell them over  
                    receiveWithin(timeToShutdown){  
                        case Offer(_, client) => client ! AuctionOver  
                        case TIMEOUT => running = false  
                    }  
            }  
        }  
    }  
}  