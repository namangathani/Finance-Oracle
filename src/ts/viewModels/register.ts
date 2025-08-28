import * as ko from "knockout";
import * as AccUtils from "../accUtils";

// ✅ Correct OJET component imports
import "ojs/ojinputtext";
import 'oj-c/input-password';
import "ojs/ojselectsingle";
import "ojs/ojbutton";
import "ojs/ojformlayout";
import "ojs/ojmessages";
import "ojs/ojhighlighttext";
import "ojs/ojlistview";
import "ojs/ojlistitemlayout";
import ArrayDataProvider = require("ojs/ojarraydataprovider");
import "ojs/ojknockout";
import "ojs/ojccheckboxset";
import "ojs/ojlabelvalue";
import "ojs/ojlabel";

interface CardType {
  value: string;
  label: string;
}

const API_BASE = "http://localhost:9090/finance-web-case-study/webapi/v1";

class RegisterViewModel {
  regName = ko.observable<string>("");
  regEmail = ko.observable<string>("");
  regUsername = ko.observable<string>("");
  regPassword = ko.observable<string>("");
  regCardType = ko.observable<string>("gold");
  cardSearchText = ko.observable<string>("");
  regCardTypeItem = ko.observable<CardType | null>(null);
  selectedCardTypes = ko.observableArray<string>(["gold"]);

  // Checkbox options
  cardTypeOptions: CardType[] = [
    { value: "gold", label: "Gold Card" },
    { value: "silver", label: "Silver Card" },
    { value: "platinum", label: "Platinum Card" }
  ];
  cardTypeDP = new ArrayDataProvider(this.cardTypeOptions, {
    keyAttributes: "value"
  });

  messages = ko.observableArray<any>([]);

  connected(): void {
    AccUtils.announce("Register page loaded.");
    document.title = "Register";
  }

  registerUser = async (): Promise<void> => {
    const user = {
      name: this.regName(),
      email: this.regEmail(),
      username: this.regUsername(),
      password: this.regPassword()
    };

    try {
      const res = await fetch(`${API_BASE}/user/register?cardType=${this.regCardType()}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user)
      });

      const data = await res.json();

      this.messages([{
        severity: res.ok ? "confirmation" : "error",
        summary: res.ok ? "Registration Successful" : "Registration Failed",
        detail: JSON.stringify(data)
      }]);

      if (res.ok) {
        this.regName("");
        this.regEmail("");
        this.regUsername("");
        this.regPassword("");
        this.regCardType("gold");
      }

    } catch (err) {
      this.messages([{
        severity: "error",
        summary: "Network Error",
        detail: String(err)
      }]);
    }
  };
}

export = RegisterViewModel;
