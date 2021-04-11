import { IPlant } from 'app/entities/plant/plant.model';

export interface IFamily {
  id?: number;
  name?: string;
  plants?: IPlant[] | null;
}

export class Family implements IFamily {
  constructor(public id?: number, public name?: string, public plants?: IPlant[] | null) {}
}

export function getFamilyIdentifier(family: IFamily): number | undefined {
  return family.id;
}
