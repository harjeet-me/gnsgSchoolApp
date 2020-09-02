import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRevenue } from 'app/shared/model/revenue.model';
import { RevenueService } from './revenue.service';
import { RevenueDeleteDialogComponent } from './revenue-delete-dialog.component';

@Component({
  selector: 'jhi-revenue',
  templateUrl: './revenue.component.html',
})
export class RevenueComponent implements OnInit, OnDestroy {
  revenues?: IRevenue[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected revenueService: RevenueService,
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
      this.revenueService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IRevenue[]>) => (this.revenues = res.body || []));
      return;
    }

    this.revenueService.query().subscribe((res: HttpResponse<IRevenue[]>) => (this.revenues = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRevenues();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRevenue): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRevenues(): void {
    this.eventSubscriber = this.eventManager.subscribe('revenueListModification', () => this.loadAll());
  }

  delete(revenue: IRevenue): void {
    const modalRef = this.modalService.open(RevenueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.revenue = revenue;
  }
}
