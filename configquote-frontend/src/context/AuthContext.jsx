import {
  createContext,
  useContext,
  useState,
} from "react";

import api, { setAccessToken } from "../api/axios";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  async function login(username, password) {
    const response = await api.post("/api/auth/login", {
      username,
      password,
    });

    const token = response.data.accessToken;

    if (!token) {
      throw new Error("Login response did not contain an access token.");
    }

    setAccessToken(token);

    try {
      const userResponse = await api.get("/api/auth/me");
      setUser(userResponse.data);
    } catch (error) {
      setAccessToken(null);
      setUser(null);
      throw error;
    }
  }

  function logout() {
    setAccessToken(null);
    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isAuthenticated: Boolean(user),
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }

  return context;
}
