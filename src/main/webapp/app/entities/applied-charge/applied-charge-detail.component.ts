import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppliedCharge } from 'app/shared/model/applied-charge.model';

@Component({
  selector: 'jhi-applied-charge-detail',
  templateUrl: './applied-charge-detail.component.html',
})
export class AppliedChargeDetailComponent implements OnInit {
  appliedCharge: IAppliedCharge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appliedCharge }) => (this.appliedCharge = appliedCharge));
  }

  previousState(): void {
    window.history.back();
  }
}
