import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {FormsModule} from "@angular/forms";
import {KeycloakService} from "../../services/services/keycloak/keycloak.service";

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  //authRequest: AuthenticationRequest = {email: '', password: ''};
  //errorMsg: Array<string> = [];
  constructor(
    private keycloakService: KeycloakService,
  ) {
  }
  async ngOnInit(): Promise<void> {
    await this.keycloakService.init();
    await this.keycloakService.login();
  }
  // protected readonly FormsModule = FormsModule;
  //
  // login() {
  //   this.errorMsg = [];
  //
  //   this.authService.authenticate({
  //       body: this.authRequest
  //     }).subscribe({
  //         next: (res) => {
  //       // Check if the response is a Blob
  //       if (res instanceof Blob) {
  //         const reader = new FileReader();
  //         reader.onload = () => {
  //           const responseText = reader.result as string;
  //           try {
  //             const jsonResponse = JSON.parse(responseText);
  //             if (jsonResponse.token) {
  //               this.tokenService.token = jsonResponse.token; // Store the token
  //               this.router.navigate(['books']);
  //             } else {
  //               console.error('Token not found in parsed response');
  //               this.errorMsg = ['Login failed: Token not found'];
  //             }
  //           } catch (e) {
  //             console.error('Error parsing Blob response:', e);
  //             this.errorMsg = ['Login failed: Invalid response format'];
  //           }
  //         };
  //         reader.readAsText(res); // Read Blob as text
  //       } else {
  //         console.error('Unexpected response format');
  //         this.errorMsg = ['Unexpected response format'];
  //       }
  //     },
  //     error: (err) => {
  //       // Handle the error case (already implemented)
  //       if (err.error instanceof Blob) {
  //         const reader = new FileReader();
  //         reader.onload = () => {
  //           const errorText = reader.result as string;
  //           try {
  //             const errorJson = JSON.parse(errorText);
  //             if (errorJson.validationErrors) {
  //               this.errorMsg = [...errorJson.validationErrors];
  //             } else if (errorJson.error) {
  //               this.errorMsg = [errorJson.error];
  //             }
  //           } catch (e) {
  //             this.errorMsg = ['An unknown error occurred.'];
  //           }
  //         };
  //         reader.readAsText(err.error);
  //       } else {
  //         // Handle other error types
  //         if (err.error?.validationErrors) {
  //           this.errorMsg = [...err.error.validationErrors];
  //         } else if (err.error?.error) {
  //           this.errorMsg = [err.error.error];
  //         } else {
  //           this.errorMsg = ['Unknown error'];
  //         }
  //       }
  //     }
  //   });
  // }
  //
  //
  // register() {
  //   this.router.navigate(['register']);
  // }
}
