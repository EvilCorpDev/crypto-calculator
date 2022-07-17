import "./styles.css"
import CryptoItem from "../CryptoItem";

const CoinsList = () => {
    return (
        <div className="mainContent">
            <div className="cryptoList">
                <CryptoItem name="Bitcoin" price="44,300" amount="0.15"/>
                <CryptoItem name="Ethereum" price="2300" amount="3.15"/>
            </div>
        </div>
    )
}

export default CoinsList