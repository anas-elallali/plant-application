import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import {Observable, Subscription} from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import {HttpClient, HttpResponse} from "@angular/common/http";
import { faFan, faUpload } from '@fortawesome/free-solid-svg-icons';

import {IFamily} from "app/entities/family/family.model";
import {FamilyService} from "app/entities/family/service/family.service";

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {

  faFan= faFan;
  faUpload = faUpload;
  panelOpenState = false;

  typesOfShoes: any[] = [];

  fileName = '';

  account: Account | null = null;
  authSubscription?: Subscription;

  constructor(private http: HttpClient,
              private accountService: AccountService,
              private familyService: FamilyService,
              private router: Router) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.familyService.query().subscribe((res: any) => {
      const familieObj: IFamily[] = res.body;
      this.typesOfShoes = familieObj.map(f => f.name);
    })
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  onFileSelected(event: any): void {

    const file: File = event.target.files[0];
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (file) {

      this.fileName = file.name;

      const formData = new FormData();

      formData.append("file", file);

      const upload$ = this.http.post("api/import", formData);

      upload$.subscribe();
    }
  }
}
