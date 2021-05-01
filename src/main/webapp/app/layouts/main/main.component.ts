import {Component, OnDestroy, OnInit} from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import {faFan, faUpload, faAddressCard, faMapPin, faChartBar, faLink, faBookReader} from "@fortawesome/free-solid-svg-icons";
import {FamilyService} from "app/entities/family/service/family.service";
import {IFamily} from "app/entities/family/family.model";
import {EventManager, EventWithContent} from "app/core/util/event-manager.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit, OnDestroy {

  subscription?: Subscription;

  faFan= faFan;
  faUpload = faUpload;
  faAddressCard = faAddressCard;
  faMapPin = faMapPin;
  faChartBar = faChartBar;
  faLink = faLink;
  faBookReader = faBookReader;
  panelOpenState = false;

  families: IFamily[] = [];

  constructor(private accountService: AccountService, private titleService: Title, private router: Router,
              private familyService: FamilyService,
              private eventManager: EventManager) {}


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

    this.getListFamilies();

    this.subscription = this.eventManager.subscribe('FamiliesList', (response: any) => {
      this.getListFamilies();
    });
  }

  ngOnDestroy(): void {
    if(this.subscription) {
      this.eventManager.destroy(this.subscription);
    }
  }

  redirectTo(uri:string): void{
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
      this.router.navigate([uri]));
  }

  private getListFamilies(): void{
    this.familyService.getFamilies().subscribe((res: any) => {
      const familieObj: IFamily[] = res.body;
      this.families = familieObj;
    });
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
