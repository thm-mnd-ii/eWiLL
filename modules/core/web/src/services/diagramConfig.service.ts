import axios, { type AxiosResponse } from 'axios'
import type DiagramConfig from '@/model/diagram/DiagramConfig'

class DiagramConfigService {
  create(diagramConfig: DiagramConfig): Promise<AxiosResponse<DiagramConfig>> {
    return axios.post('/api/diagramConfig/', diagramConfig)
  }

  get(id: number): Promise<AxiosResponse<DiagramConfig>> {
    return axios.get('/api/diagramConfig/' + id)
  }

  getAll(): Promise<AxiosResponse<DiagramConfig[]>> {
    return axios.get('/api/diagramConfig/all')
  }

  update(id: number): Promise<AxiosResponse<DiagramConfig>> {
    return axios.put('/api/diagramConfig/' + id)
  }

  delete(id: number): Promise<AxiosResponse<DiagramConfig>> {
    return axios.delete('/api/diagramConfig/' + id)
  }
}

export default new DiagramConfigService()
