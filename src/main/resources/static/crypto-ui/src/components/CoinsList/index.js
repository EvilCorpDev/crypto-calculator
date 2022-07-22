import "./styles.css"
import CryptoItem from "../CryptoItem";
import useFetch from "../../useFetch";
import Cookies from "universal-cookie/lib";

const CoinsList = () => {
    const cookies = new Cookies();
    const {data: coins, error, pending} = useFetch("/coins/price", {'Authorization':  `Bearer ${cookies.get('token')}`});

    const handleReloadClick = () => {
        fetch("/operations/refresh", {
            method: "GET",
            headers: {'Authorization':  `Bearer ${cookies.get('token')}`}
        }).then(resp => {
            if (resp.ok) {
                return resp.json()
            } else {
                console.log(resp.statusMessage)
            }
        }).then(data => {
            coins.length = 0;
            coins.push(...data);
        })
    }

    return (
        <div className="mainContent">
            <div className="refreshList">
                <img className="smallImage" src="reload.png" onClick={handleReloadClick}/>
            </div>
            <div className="cryptoList">
                {coins && coins.map((item, idx) => (
                    <CryptoItem key={idx} name={item.coin.name} symbol={item.coin.symbol} price={item.averagePrice} amount={item.amount}/>
                ))}
            </div>
        </div>
    )
}

export default CoinsList