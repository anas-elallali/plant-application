import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import {faFan, faUpload} from "@fortawesome/free-solid-svg-icons";
import {HttpClient} from "@angular/common/http";
import {FamilyService} from "app/entities/family/service/family.service";
import {IFamily} from "app/entities/family/family.model";

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  faFan= faFan;
  faUpload = faUpload;
  panelOpenState = false;

  families: IFamily[] = [];

  constructor(private accountService: AccountService, private titleService: Title, private router: Router,
              private familyService: FamilyService,) {}


  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });

    this.familyService.query().subscribe((res: any) => {
      const familieObj: IFamily[] = res.body;
      this.families = familieObj;
    })
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'Plant Application';
    }
    this.titleService.setTitle(pageTitle);
  }
}
