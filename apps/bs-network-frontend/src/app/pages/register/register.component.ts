import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RegistrationRequest} from "../../services/models/registration-request";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthentificationService} from "../../services/services/authentification.service";

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  registerRequest: RegistrationRequest = {email:"", firstname:"", lastname:"", password:""}
  errorMsg: Array<string> = [];
  constructor(
    private router: Router,
    private authService: AuthentificationService,
  ) {
  }

  login() {
    this.router.navigate(['login']);
  }

  register() {
    this.errorMsg = [];

    this.authService.register({
      body: this.registerRequest,
    }).subscribe({
      next: () => {
        this.router.navigate(['activate-account']);
      },
      error: (err) => {
        if (err.error instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            const errorText = reader.result as string;
            try {
              const errorJson = JSON.parse(errorText);
              if (errorJson.validationErrors) {
                this.errorMsg = [...errorJson.validationErrors];
              } else if (errorJson.error) {
                this.errorMsg = [errorJson.error];
              } else {
                this.errorMsg = ['An unknown error occurred.'];
              }
            } catch (e) {
              this.errorMsg = ['An unknown error occurred.'];
            }
          };
          reader.readAsText(err.error);
        } else {
          if (err.error?.validationErrors) {
            this.errorMsg = [...err.error.validationErrors];
          } else if (err.error?.error) {
            this.errorMsg = [err.error.error];
          } else {
            this.errorMsg = ['Unknown error'];
          }
        }
      }
    });
  }

}
