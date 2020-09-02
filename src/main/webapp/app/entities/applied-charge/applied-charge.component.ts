import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppliedCharge } from 'app/shared/model/applied-charge.model';
import { AppliedChargeService } from './applied-charge.service';
import { AppliedChargeDeleteDialogComponent } from './applied-charge-delete-dialog.component';

@Component({
  selector: 'jhi-applied-charge',
  templateUrl: './applied-charge.component.html',
})
export class AppliedChargeComponent implements OnInit, OnDestroy {
  appliedCharges?: IAppliedCharge[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected appliedChargeService: AppliedChargeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.appliedChargeService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IAppliedCharge[]>) => (this.appliedCharges = res.body || []));
      return;
    }

    this.appliedChargeService.query().subscribe((res: HttpResponse<IAppliedCharge[]>) => (this.appliedCharges = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAppliedCharges();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAppliedCharge): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAppliedCharges(): void {
    this.eventSubscriber = this.eventManager.subscribe('appliedChargeListModification', () => this.loadAll());
  }

  delete(appliedCharge: IAppliedCharge): void {
    const modalRef = this.modalService.open(AppliedChargeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appliedCharge = appliedCharge;
  }
}
