export interface ITarea {
  id?: string;
  nombre?: string | null;
  descripcion?: string | null;
  fechaLimite?: Date | null;
}

export class Tarea implements ITarea {
  constructor(
    public id?: string,
    public nombre?: string | null,
    public descripcion?: string | null,
    public fechaLimite?: Date | null,
  ) {}
}
