import Header from "./components/Header";
import CoinsList from "./components/CoinsList";
import Login from "./components/Login";
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Header/>
                <Routes>
                    <Route path="/coins" element={<CoinsList/>} />
                    <Route path="/login" element={<Login/>} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
