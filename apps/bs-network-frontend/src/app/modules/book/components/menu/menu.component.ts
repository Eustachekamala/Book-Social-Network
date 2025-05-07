import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {KeycloakService} from "../../../../services/services/keycloak/keycloak.service";

@Component({
  selector: 'app-menu',
  imports: [CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
})
export class MenuComponent implements OnInit {
  constructor(
    private keycloakService: KeycloakService,
  ) {
  }
  ngOnInit() {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      });
    });
  }

  async logout() {
    await this.keycloakService.logout();
  }
}
