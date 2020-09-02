import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IASPath } from 'app/shared/model/as-path.model';

@Component({
  selector: 'jhi-as-path-detail',
  templateUrl: './as-path-detail.component.html',
})
export class ASPathDetailComponent implements OnInit {
  aSPath: IASPath | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aSPath }) => (this.aSPath = aSPath));
  }

  previousState(): void {
    window.history.back();
  }
}
