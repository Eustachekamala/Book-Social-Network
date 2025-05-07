import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
// @ts-ignore
import Keycloak from 'keycloak-js';
import {UserProfile} from "./user-profile";

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;
  private _profile: UserProfile | undefined;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  get keycloak(): Keycloak | undefined {
    if (!this._keycloak && isPlatformBrowser(this.platformId)) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9090',
        realm: 'bs-network',
        clientId: 'b-service-network',
      });
    }
    return this._keycloak;
  }

  get userProfile(): UserProfile | undefined {
    return this._profile;
  }

  async init() {
    if (!isPlatformBrowser(this.platformId)) {
      // Avoid initializing Keycloak on the server
      console.warn('Keycloak init skipped: not in browser');
      return;
    }

    console.log('Authenticating the user...');
    const authenticated = await this.keycloak?.init({
      onLoad: 'login-required',
    });

    if (authenticated) {
      this._profile = (await  this._keycloak?.loadUserProfile()) as UserProfile;
      this._profile.token = this.keycloak?.token;
      console.log('User authenticated');
    }
  }
  login() {
    return this._keycloak?.login();
  }

  logout() {
    return this._keycloak?.logout({redirectUri: 'http://localhost:4200'});
  }
}
