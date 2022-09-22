import "./styles.css"
import CryptoItem from "../CryptoItem";
import useFetch from "../../useFetch";
import Cookies from "universal-cookie/lib";

const CoinsList = () => {
    const cookies = new Cookies();
    const {
        data: myWallet,
        setData: setMyWallet,
        error,
        pending
    } = useFetch("/coins/price", {'Authorization': `Bearer ${cookies.get('token')}`});

    const handleReloadClick = () => {
        fetch("/operations/refresh", {
            method: "GET",
            headers: {'Authorization': `Bearer ${cookies.get('token')}`}
        }).then(resp => {
            if (resp.ok) {
                return resp.json()
            } else {
                console.log(resp.statusMessage)
            }
        }).then(data => {
            setMyWallet(data);
        })
    }

    const handleFilterChange = (filter) => {
        console.log(filter)
        const newCoins = myWallet.coins.map(item => {
            item.show = !!(filter === ''
                || item.coin.name.toLowerCase().includes(filter.toLowerCase())
                || item.coin.symbol.toLowerCase().includes(filter.toLowerCase()));
            return item
        })
        setMyWallet({...myWallet, coins: newCoins})
    }

    return (
        <div className="mainContent">
            <div className="refreshList">
                <img className="smallImage" src="/reload.png" onClick={handleReloadClick} alt="Reload button"/>
            </div>
            <div className="Total data">
                <h4>Total money: {myWallet && myWallet.totalValue}</h4>
                <p>Total number of coins: {myWallet && myWallet.numberOfCoins}</p>
            </div>
            <div className="searchBox">
                <input type="text" placeholder="Filter coins" onChange={(event) => {
                    handleFilterChange(event.target.value);
                }}/>
            </div>
            <div className="cryptoList">
                {myWallet && myWallet.coins.map((item, idx) => {
                    if (item.show === undefined || item.show) {
                        return (<CryptoItem key={idx} name={item.coin.name} symbol={item.coin.symbol}
                                            price={item.averagePrice}
                                            amount={item.amount} totalValue={item.totalValue}/>
                        )
                    }
                })}
            </div>
        </div>
    )
}

export default CoinsList