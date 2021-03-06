import "./styles.css"
import {Link} from "react-router-dom";

const CryptoItem = (props) => {
    const {name, symbol, price, amount, totalValue} = props;
    return (
        <Link to={`/coin/${symbol}`}>
            <div className="cryptoItem">
                <div className="w80">
                    <h3>{name}</h3>
                    <p>{symbol}</p>
                    <p>Average Price: ${price}</p>
                    <p>Total Value: ${totalValue}</p>
                    <p>Amount: {amount}</p>
                </div>
                <div className="w20">
                    <div className="coinLogo">
                        <img
                            src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/128px-Bitcoin.svg.png"
                            alt="logo"></img>
                    </div>
                </div>
            </div>
        </Link>
    );
}

export default CryptoItem