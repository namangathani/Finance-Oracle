import * as ko from "knockout";
import * as AccUtils from "../accUtils";

const API_BASE = "http://localhost:9090/finance-web-case-study/webapi/v1";

class AdminLoginViewModel {
  username = ko.observable("");
  password = ko.observable("");
  messages = ko.observableArray<any>([]);

  connected(): void {
    AccUtils.announce("Admin Login page loaded.");
    document.title = "Admin Login";
  }

  loginAdmin = async (): Promise<void> => {
    try {
      const res = await fetch(`${API_BASE}/admin/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: this.username(), password: this.password() }),
      });
      const text = await res.text();
      this.messages([{
        severity: res.ok ? "confirmation" : "error",
        summary: res.ok ? "Login Success" : "Login Failed",
        detail: text,
      }]);
    } catch (err) {
      this.messages([{ severity: "error", summary: "Network Error", detail: String(err) }]);
    }
  };
}

export = AdminLoginViewModel;
