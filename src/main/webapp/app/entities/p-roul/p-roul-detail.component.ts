import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPRoul } from 'app/shared/model/p-roul.model';

@Component({
  selector: 'jhi-p-roul-detail',
  templateUrl: './p-roul-detail.component.html',
})
export class PRoulDetailComponent implements OnInit {
  pRoul: IPRoul | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pRoul }) => (this.pRoul = pRoul));
  }

  previousState(): void {
    window.history.back();
  }
}
