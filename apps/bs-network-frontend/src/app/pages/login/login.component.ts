import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthentificationService} from "../../services/services/authentification.service";

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMsg: Array<string> = [];
  constructor(
    private router: Router,
    private authService: AuthentificationService,
  ) {
  }
  protected readonly FormsModule = FormsModule;

  login() {
    this.errorMsg = [];

    this.authService.authenticate({ body: this.authRequest }).subscribe({
      next: (res) => {
        this.router.navigateByUrl('books');
      },
      error: async (err) => {
        // If error is a blob (meaning Angular didn't parse it)
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
              }
            } catch (e) {
              this.errorMsg = ['An unknown error occurred.'];
            }
          };
          reader.readAsText(err.error);
        } else {
          // Normal path
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

  register() {
    this.router.navigate(['register']);
  }
}
