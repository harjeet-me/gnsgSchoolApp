import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from './expense.service';
import { ExpenseDeleteDialogComponent } from './expense-delete-dialog.component';

@Component({
  selector: 'jhi-expense',
  templateUrl: './expense.component.html',
})
export class ExpenseComponent implements OnInit, OnDestroy {
  expenses?: IExpense[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected expenseService: ExpenseService,
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
      this.expenseService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IExpense[]>) => (this.expenses = res.body || []));
      return;
    }

    this.expenseService.query().subscribe((res: HttpResponse<IExpense[]>) => (this.expenses = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExpenses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExpense): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExpenses(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseListModification', () => this.loadAll());
  }

  delete(expense: IExpense): void {
    const modalRef = this.modalService.open(ExpenseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.expense = expense;
  }
}
