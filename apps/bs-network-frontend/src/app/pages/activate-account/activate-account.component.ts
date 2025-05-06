import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthentificationService} from "../../services/services/authentification.service";
import {Router} from "@angular/router";
import {CodeInputModule} from "angular-code-input";

@Component({
  selector: 'app-activate-account',
  imports: [CommonModule, CodeInputModule],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.css',
})
export class ActivateAccountComponent {
  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthentificationService
  ) {
  }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  private confirmAccount(token: string) {
    this.authService.confirm({
      token
    }).subscribe({
      next: () => {
        this.message = "Your account has been successfully activated.\nNow you can proceed to login.";
        this.submitted = true;
        this.isOkay = true;
      },
      error: () => {
        this.message = "Token has been expired or invalid.";
        this.submitted = true;
        this.isOkay = false;
      }
    })
  }
}
