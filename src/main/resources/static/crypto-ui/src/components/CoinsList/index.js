import "./styles.css"
import CryptoItem from "../CryptoItem";
import useFetch from "../../useFetch";
import Cookies from "universal-cookie/lib";

const CoinsList = () => {
    const cookies = new Cookies();
    const {data: coins, error, pending} = useFetch("/coins/price", {'Authorization':  `Bearer ${cookies.get('token')}`});

    return (
        <div className="mainContent">
            <div className="cryptoList">
                {coins && coins.map((item, idx) => (
                    <CryptoItem key={idx} name={item.coin.name} price={item.averagePrice} amount={item.amount}/>
                ))}
            </div>
        </div>
    )
}

export default CoinsList