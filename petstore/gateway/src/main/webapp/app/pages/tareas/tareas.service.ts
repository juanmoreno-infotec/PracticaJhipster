import type { ITarea } from '@/shared/model/tarea.model';

import axios from 'axios';

const baseApiUrl = 'services/tareams/api/tareas';

export default class TareaService {
  public listar(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public crear(tarea: ITarea): Promise<ITarea> {
    return new Promise<ITarea>((resolve, rejects) => {
      axios
        .post(`${baseApiUrl}`, tarea)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          rejects(err);
        });
    });
  }

  public borrar(tarea: ITarea): Promise<ITarea> {
    return new Promise<ITarea>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${tarea.id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public actualizar(tarea: ITarea): Promise<ITarea> {
    return new Promise<ITarea>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${tarea.id}`, tarea)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
