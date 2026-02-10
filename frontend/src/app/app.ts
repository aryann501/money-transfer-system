import { Component } from '@angular/core';
import { TransferMoneyComponent } from './transfer-money/transfer-money';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [TransferMoneyComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App { }