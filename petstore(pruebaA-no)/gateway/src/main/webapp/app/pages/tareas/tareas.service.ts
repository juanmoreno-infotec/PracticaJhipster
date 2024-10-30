
//Agregamos al ruta del modelo tarea
import type { ITarea } from "@/shared/model/tarea.model";

import axios from 'axios';

const baseApiUrl = 'services/tareams/api/tareas';

export default class TareaService {
    // Agregamos la promesa del listado
    public listar(): Promise<any> {
        return new Promise<any>((resolve, reject) => {
            axios //acciones
                // Obtiene
                .get(`${baseApiUrl}`)
                // Resuelve
                .then( res => {
                    resolve(res);
                })
                //Atrapa
                .catch(err => {
                    reject(err)
                });
        });
    }

    // Promesa de crear la tarea
    public crear(tarea:Itarea): Promise<ITarea> {
        return new Promise<ITarea>((resolve, rejects) => {
            axios // acciones
                // Publica
                .post(`${baseApiUrl}`, tarea)
                // Resuelve
                .then(res => {
                    resolve(res.data);
                })
                .catch(err => {
                    rejects(err);
                });
        });
    }
}