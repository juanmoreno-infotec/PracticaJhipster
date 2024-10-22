export interface ICalculator {
  number1?: number;
  number2?: number;
  result?: number;
  operation?: string;
  history?: Array<{ operation: string, result: number }>;
}

export class Calculator implements ICalculator {
  constructor(
    public number1?: number,
    public number2?: number,
    public result?: number,
    public operation?: string,
    public history?: Array<{ operation: string, result: number }>,
  ) {}
}