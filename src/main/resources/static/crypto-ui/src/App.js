import Header from "./components/Header";
import CoinsList from "./components/CoinsList";
import Login from "./components/Login";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import UserPage from "./components/UserPage";
import OrderHistory from "./components/OrderHistory";

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Header/>
                <Routes>
                    <Route path="/coin/*" element={<OrderHistory/>} />
                    <Route path="/login" element={<Login/>} />
                    <Route path="/profile" element={<UserPage/>} />
                    <Route exact path="/coins" element={<CoinsList/>} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
