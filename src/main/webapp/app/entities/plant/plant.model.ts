import { IEcologicalStatus } from 'app/entities/ecological-status/ecological-status.model';
import { IFamily } from 'app/entities/family/family.model';

export interface IPlant {
  id?: number;
  scientificName?: string;
  synonym?: string | null;
  localName?: string | null;
  englishName?: string | null;
  voucherNumber?: string;
  pictureContentType?: string | null;
  picture?: string | null;
  botanicalDescription?: string | null;
  therapeuticUses?: string | null;
  usedParts?: string | null;
  preparation?: string | null;
  pharmacologicalActivities?: string | null;
  majorPhytochemicals?: string | null;
  ecologicalStatus?: IEcologicalStatus | null;
  family?: IFamily;
}

export class Plant implements IPlant {
  constructor(
    public id?: number,
    public scientificName?: string,
    public synonym?: string | null,
    public localName?: string | null,
    public englishName?: string | null,
    public voucherNumber?: string,
    public pictureContentType?: string | null,
    public picture?: string | null,
    public botanicalDescription?: string | null,
    public therapeuticUses?: string | null,
    public usedParts?: string | null,
    public preparation?: string | null,
    public pharmacologicalActivities?: string | null,
    public majorPhytochemicals?: string | null,
    public ecologicalStatus?: IEcologicalStatus | null,
    public family?: IFamily
  ) {}
}

export function getPlantIdentifier(plant: IPlant): number | undefined {
  return plant.id;
}
