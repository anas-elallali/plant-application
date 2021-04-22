import { EcologicalStatusType } from 'app/entities/enumerations/ecological-status-type.model';

export interface IEcologicalStatus {
  id?: number;
  name?: string;
}

export class EcologicalStatus implements IEcologicalStatus {
  constructor(public id?: number, public name?: string) {}
}

export function getEcologicalStatusIdentifier(ecologicalStatus: IEcologicalStatus): number | undefined {
  return ecologicalStatus.id;
}
