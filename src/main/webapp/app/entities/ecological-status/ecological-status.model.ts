import { EcologicalStatusType } from 'app/entities/enumerations/ecological-status-type.model';

export interface IEcologicalStatus {
  id?: number;
  name?: EcologicalStatusType;
}

export class EcologicalStatus implements IEcologicalStatus {
  constructor(public id?: number, public name?: EcologicalStatusType) {}
}

export function getEcologicalStatusIdentifier(ecologicalStatus: IEcologicalStatus): number | undefined {
  return ecologicalStatus.id;
}
