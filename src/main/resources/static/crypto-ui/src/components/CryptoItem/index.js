import "./styles.css"

const CryptoItem = (props) => {
    const {name, price, amount} = props;
    return (
        <div className="cryptoItem">
            <div className="w80">
                <h3>{name}</h3>
                <p>Average Price: ${price}</p>
                <p>Amount: {amount}</p>
            </div>
            <div className="w20">
                <div className="coinLogo">
                    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/128px-Bitcoin.svg.png" alt="logo"></img>
                </div>
            </div>
        </div>
    );
}

export default CryptoItem