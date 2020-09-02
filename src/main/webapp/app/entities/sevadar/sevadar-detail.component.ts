import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISevadar } from 'app/shared/model/sevadar.model';

@Component({
  selector: 'jhi-sevadar-detail',
  templateUrl: './sevadar-detail.component.html',
})
export class SevadarDetailComponent implements OnInit {
  sevadar: ISevadar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sevadar }) => (this.sevadar = sevadar));
  }

  previousState(): void {
    window.history.back();
  }
}
