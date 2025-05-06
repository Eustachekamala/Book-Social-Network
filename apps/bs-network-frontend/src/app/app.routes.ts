import { Route } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";

export const appRoutes: Route[] = [
  {
    path: 'login',
    component: LoginComponent
  }
];
