import Cookies from "universal-cookie/lib";
import useFetch from "../../useFetch";
import {useLocation} from "react-router-dom";
import "./styles.css"

const OrderHistory = () => {
    const cookies = new Cookies();
    const location = useLocation();
    const symbol = location.pathname.substring("/coin/".length);
    const {
        data: history,
        error,
        pending
    } = useFetch(`/operations/${symbol}`, {'Authorization': `Bearer ${cookies.get('token')}`});

    return (
        <div className="orderHistory">
            {history && history.map((item, idx) => (
                <div className="order" key={idx}>
                    <h3>{item.name}</h3>
                    <p>{item.symbol}</p>
                    <p>Price: ${item.price}</p>
                    <p>Amount: {item.amount}</p>
                    <p>Type: {item.type}</p>
                    <p>Date: {item.date}</p>
                </div>
            ))}
        </div>
    )
}

export default OrderHistory;