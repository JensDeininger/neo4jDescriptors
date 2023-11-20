// A cypher script to create a proposal master graph set up.

// Create two top holder nodes.
CREATE (graphTopNode1:Meta:MasterGraphNode {
        name: "Meta Data Graph",
        number: "MAS001",
        description: "Full of all of the meta data inputs needed for the UI."
    }),
    (graphTopNode2:MasterGraphNode {
        name: "Run Graph",
        number: "MAS002",
        description: "Full of all of the data used for the run."
    }),
    (graphTopNode3:MasterGraphNode {
        name: "Company Data Graph",
        number: "MAS003",
        description: "Full of all of company and project detail."
    })
// Create an edge to connect up the top holder nodes (just for a central point in reviewing the graph).
// CREATE
//     (graphTopNode1)-[:LINKED]->(graphTopNode2),
//     (graphTopNode2)-[:LINKED]->(graphTopNode3),
//     (graphTopNode3)-[:LINKED]->(graphTopNode1)


// Master Input Graph Data

// Add Bodystyle.
// Add a top node for Bodystyle which holds the required fields and data types.
CREATE (BodystyleTopNode:Meta:Bodystyle:TopNode {
        // id: "integer",
		// name: "string",
        number: "string",
		displayName: "string",
        numberSeats: "integer",
        numberBenches: "integer",
        numberDoors: "integer",
        complexity: "long",
        visualOrder: "integer",
		displayInformation: "string"
    })
// Add all Bodystyles.
CREATE (bodystyle1:Meta:Bodystyle {
        // id: 2,
        // name: "3door", // Do we need this?
        number: "BD002", // Proposal.
        displayName: "3 Door",
        numberSeats: 2,
        numberBenches: 1,
        numberDoors: 3,
        complexity: 0.31,
        visualOrder: 2,
        displayInformation: "A three door vehicle typically has a regular door on each side of the vehicle and a rear hatchback which opens."
    }),
    (bodystyle6:Meta:Bodystyle {
        // id: 2,
        // name: "SUV", // Do we need this?
        number: "BD006", // Proposal.
        displayName: "SUV",
        numberSeats: 2,
        numberBenches: 1,
        numberDoors: 5,
        complexity: 0.56,
        visualOrder: 6,
        displayInformation: "A sport utility vehicle (SUV) typically extends a road based passenger vehicle into an off-road vehicle by adding increased off-road capability through increased ground clearance and the addition of four-wheel drive."
    })
// Create an edge to connect up to the master nodes.
CREATE
    (BodystyleTopNode)-[:INPUT]->(graphTopNode1),
    (bodystyle1)-[:INPUT]->(BodystyleTopNode),
    (bodystyle6)-[:INPUT]->(BodystyleTopNode)

// Add Car Division.
// Add a top node for Car Division which holds the required fields and data types.
CREATE (CarDivisionTopNode:Meta:CarDivision:TopNode {
        // id: "integer",
		name: "string",
		displayName: "string",
		displayInformation: "string"
    })
// Add all Car Divisions.
CREATE (carDivision1:Meta:CarDivision {
        // id: 3,
		name: "PWT", // Proposal to add Powertrain as another division next to platform and top hat.
		displayName: "Powertrain",
		displayInformation: "The powertrain includes the components and systems that make up the engine/battery and drivetrain of the vehicle."
    })
// Create an edge to connect up to the master nodes.
CREATE
    (CarDivisionTopNode)-[:INPUT]->(graphTopNode1),
    (carDivision1)-[:INPUT]->(CarDivisionTopNode)

// Add Commodities.
// Add a top node for Commodity which holds the required fields and data types.
CREATE (CommodityTopNode:Meta:Commodity:TopNode {
        // id: "integer",
		name: "string",
        weight: "integer",
        carDivision: "integer",
        visualOrder: "integer",
		displayInformation: "string"
    })
// Add all Commodities.
CREATE (commodity1:Commodity:Level1Commodity:Meta {
        // id: 1,
		name: "Platform",
        weight: 1,
		// carDivision: 1, // Remove if removing car division?
        visualOrder: 1,
		displayInformation: "The platform typically includes the components and systems that make up the lower half of the vehicle including the underbody and suspension systems."
    }),
    (commodity2:Commodity:Level1Commodity:Meta {
        // id: 2,
		name: "Top Hat",
        weight: 1,
		// carDivision: 2, // Remove if removing car division?
        visualOrder: 2,
		displayInformation: "The top hat typically includes the components and systems that make up the upper half of the vehicle including the body work, closures and interior."
    }),
    (commodity3:Commodity:Level1Commodity:Meta {
        // id: 3,
		name: "Powertrain",
        weight: 0.8,
		// carDivision: 3, // Remove if removing car division?
        visualOrder: 3,
		displayInformation: "The powertrain includes the components and systems that make up the engine/battery and drivetrain of the vehicle."
    }),
    (commodity4:Commodity:Level2Commodity:Meta {
        // id: 4,
		name: "Front Structure",
        weight: 1,
		// carDivision: 1, // Remove if removing car division?
        visualOrder: 4,
		displayInformation: "The front structure includes the components and systems that make up the front crash impact system."
    }),
    (commodity5:Commodity:Level3Commodity:Meta {
        // id: 5,
		name: "Front Beam",
        weight: 2,
		// carDivision: 1, // Remove if removing car division?
        visualOrder: 5,
		displayInformation: "The front beam takes the majority of the load in a front crash impact."
    }),
    (commodity6:Commodity:Level3Commodity:Meta {
        // id: 6,
		name: "Shotgun Beams",
        weight: 1.5,
		// carDivision: 1, // Remove if removing car division?
        visualOrder: 6,
		displayInformation: "The shotgun beams carry a small part of the load in a front crash impact."
    })
// Create an edge to connect up to the master nodes.
CREATE
    (CommodityTopNode)-[:INPUT]->(graphTopNode1),
    (commodity1)-[:INPUT]->(CommodityTopNode),
    (commodity2)-[:INPUT]->(CommodityTopNode),
    (commodity3)-[:INPUT]->(CommodityTopNode)
// Create edges to connect up the commodities.
CREATE
    (commodity4)-[:SUB_COMMODITY_OF]->(commodity1),
    (commodity5)-[:SUB_COMMODITY_OF]->(commodity4),
    (commodity6)-[:SUB_COMMODITY_OF]->(commodity4)


// Add Regions.
// Add a top node for Regions which holds the required fields and data types.
CREATE (RegionTopNode:Region:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all regions.
CREATE (region1:Region:Meta {
        // id: 1,
		name: "North America",
        number: "RE001", // Proposed to add.
		acronym: "NAm",
		complexity: 1.5,
        regionCAEComplexity: 1.1,
        regionPhysicalRequirementsComplexity: 1.0,

        // Properties from "cypher/dbinit/initialsetup/connectcountriesregions.cypher".
        numberCountries: 1, // As this number won't change, I'd argue we should hard code? Or leave as dynamic for Russia's sake?
        numberGreenFieldPlants: 0.0, // Do we need to set this property dynamically or better to have all 0 to begin with and then change?
		numberBrownFieldPlants: 0.0 // Same as question above.
    }),
    (region5:Region:Meta {
        // id: 5,
		name: "European Union", 
        number: "RE005", // Proposed to add.
		acronym: "EUn",
		complexity: 2,
        regionCAEComplexity: 1,
        regionPhysicalRequirementsComplexity: 1.0,

        // Properties from "cypher/dbinit/initialsetup/connectcountriesregions.cypher".
        numberCountries: 1, // As this number won't change, I'd argue we should hard code? Or leave as dynamic for Russia's sake?
        numberGreenFieldPlants: 0.0, // Do we need to set this property dynamically or better to have all 0 to begin with and then change?
		numberBrownFieldPlants: 0.0 // Same as question above.
    })
// Create an edge to connect up to the master node.
CREATE
    (RegionTopNode)-[:INPUT]->(graphTopNode1),
    (region1)-[:INPUT]->(RegionTopNode),
    (region5)-[:INPUT]->(RegionTopNode)


// Add Countries.
// Add a top node for Countries which holds the required fields and data types.
CREATE (CountryTopNode:Country:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all Countries.
CREATE (country1:Country:Meta {
        // id: 1,
		// region: 1, // No longer needed as edge exists.
        name: "United States",
        number: "CO001", // Proposed to add.
        acronym: "US",
        area: 9629047.0, // Doesn't need to be updated live unless its Russia.
        population: 326766748.0, // Have this updated live from online if we use it for calculations. 
        GDPPerCapita: 59800.0,
        complexity: 1, // To be discussed if we need if region already has complexity. Keep both? 
        carAdmissions2018: 17320000.0, // Have this updated live from online if we use it for calculations.

        // Properties from "cypher/dbinit/initialsetup/connectcountriesregions.cypher".
        region: "North America", // Do we need this if the country is already connected up to the region?
		regionAcronym: "NAm", // Same as question above.
		regionComplexity: 1.1, // Same as question above.
		homeRegion: false, // Do we need to set this property dynamically or better to have all false to begin with and then change one to true?
		numberGreenFieldPlants: 0.0, // Same as question above.
		numberBrownFieldPlants: 0.0, // Same as question above.
		targeted: false // What does this do?
    }),
    (country2:Country:Meta {
        // id: 2,
		// region: 1, // No longer needed as edge exists.
        name: "Canada",
        number: "CO002", // Proposed to add.
        acronym: "CA",
        area: 9976137.0, // Doesn't need to be updated live unless its Russia.
        population: 34834841.0, // Have this updated live from online if we use it for calculations. 
        GDPPerCapita: 48400.0,
        complexity: 1, // To be discussed if we need if region already has complexity. Keep both? 
        carAdmissions2018: 2010000.0, // Have this updated live from online if we use it for calculations.

        // Properties from "cypher/dbinit/initialsetup/connectcountriesregions.cypher".
        region: "North America", // Do we need this if the country is already connected up to the region?
		regionAcronym: "NAm", // Same as question above.
		regionComplexity: 1.1, // Same as question above.
		homeRegion: false, // Do we need to set this property dynamically or better to have all false to begin with and then change one to true?
		numberGreenFieldPlants: 0.0, // Same as question above.
		numberBrownFieldPlants: 0.0, // Same as question above.
		targeted: false // What does this do?
    }),
    (country41:Country:Meta {
        // id: 41,
		// region: 5, // No longer needed as edge exists.
        name: "Germany",
        number: "CO041", // Proposed to add.
        acronym: "DE",
        area: 357021, // Doesn't need to be updated live unless its Russia.
        population: 82293457, // Have this updated live from online if we use it for calculations. 
        GDPPerCapita: 50800,
        complexity: 1, // To be discussed if we need if region already has complexity. Keep both? 
        carAdmissions2018: 3720000, // Have this updated live from online if we use it for calculations.

        // Properties from "cypher/dbinit/initialsetup/connectcountriesregions.cypher".
        region: "European Union", // Do we need this if the country is already connected up to the region?
		regionAcronym: "EUn", // Same as question above.
		regionComplexity: 1.0, // Same as question above.
		homeRegion: false, // Do we need to set this property dynamically or better to have all false to begin with and then change one to true?
		numberGreenFieldPlants: 0.0, // Same as question above.
		numberBrownFieldPlants: 0.0, // Same as question above.
		targeted: false // What does this do?
    })
// Create an edge to connect up to the master node.
CREATE
    (CountryTopNode)-[:INPUT]->(graphTopNode1),
    (country1)-[:INPUT]->(CountryTopNode),
    (country2)-[:INPUT]->(CountryTopNode),
    (country41)-[:INPUT]->(CountryTopNode)
// Create an edge to connect up the Country to the relevant Region node.
CREATE
    (country1)-[:BELONGS_TO_REGION]->(region1), // Name from "cypher/dbinit/initialsetup/connectcountriesregions.cypher".
    (country2)-[:BELONGS_TO_REGION]->(region1),
    (country41)-[:BELONGS_TO_REGION]->(region5)

// Add WorkingHoursModel.
// Add a top node for WorkingHoursModels which holds the required fields and data types.
CREATE (WorkingHoursModelTopNode:WorkingHoursModel:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all WorkingHoursModels.
CREATE (workingHoursModel1:Meta:WorkingHoursModel {
        // id: 2,
		name: "Double Shift",
		efficiencyRating: 0.9,
		displayInformation: "Two teams work across the day with a single handover period between."
    })
// Create an edge to connect up to the master node.
CREATE
    (WorkingHoursModelTopNode)-[:INPUT]->(graphTopNode1),
    (workingHoursModel1)-[:INPUT]->(WorkingHoursModelTopNode)


// Add CompanySize.
// Add a top node for WorkingHoursModels which holds the required fields and data types.
CREATE (CompanySizeTopNode:CompanySize:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all CompanySizes
CREATE (companySize1:CompanySize:Meta {
        // id: 1,
		name: "> 2000",
		value: 3.0,
        description: "Large",
		displayName: "Large (> 2000)"
    }),
    (companySize2:CompanySize:Meta {
        // id: 2,
		name: "100 - 2000",
		value: 2.0,
        description: "Medium",
		displayName: "Medium (100 - 2000)"
    })
// Create an edge to connect up to the master node.
CREATE
    (CompanySizeTopNode)-[:INPUT]->(graphTopNode1),
    (companySize1)-[:INPUT]->(CompanySizeTopNode),
    (companySize2)-[:INPUT]->(CompanySizeTopNode)


// Add CompanyStructuredness.
// Add a top node for CompanyStructuredness' which holds the required fields and data types.
CREATE (CompanyStructurednessTopNode:CompanyStructuredness:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all CompanyStructuredness'.
CREATE (companyStructuredness1:CompanyStructuredness:Meta {
        // id: 1,
		name: "Well structured",
		value: 1,
		displayInformation: "A well structured company has an excellent level of organisation in place with the correct systems set up to allow easy communication between departments and pass information from executive level down through the company to ensure company targets are met."
    }),
    (companyStructuredness2:CompanyStructuredness:Meta {
        // id: 2,
		name: "Some structured",
		value: 2,
		displayInformation: "A company with some structure has some experience in delivering a product and has a number of systems in place to support communication between departments. Information is typically passed well down through the company."
    })
// Create an edge to connect up to the master node.
CREATE
    (CompanyStructurednessTopNode)-[:INPUT]->(graphTopNode1),
    (companyStructuredness1)-[:INPUT]->(CompanyStructurednessTopNode),
    (companyStructuredness2)-[:INPUT]->(CompanyStructurednessTopNode)


// Add TechnologyLevel.
// Add a top node for TechnologyLevels which holds the required fields and data types.
CREATE (TechnologyLevelTopNode:TechnologyLevel:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all TechnologyLevels.
CREATE (technologyLevel1:TechnologyLevel:Meta {
        // id: 2,
		name: "Mostly automated",
		value: 2,
		displayInformation: "A company with a mostly automated manufacturing plant has high levels of automation in place with minimal manual labour supporting production. Manufacturing and assembly can be achieved at a increased plant capacity rate in reduced timing."
    })
// Create an edge to connect up to the master node.
CREATE
    (TechnologyLevelTopNode)-[:INPUT]->(graphTopNode1),
    (technologyLevel1)-[:INPUT]->(TechnologyLevelTopNode)


// Add Edginess.
// Add a top node for Edginess' which holds the required fields and data types.
CREATE (EdginessTopNode:Edginess:Meta:TopNode { // Rename entirely from edginess?
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all Edginess's.
CREATE (edginess1:Edginess:Meta {
        // id: 3,
		name: "Medium complexity",
		value: 3.0,
		displayInformation: "A vehicle that has styling surfaces that are medium complexity is defined as a styling design that has a mix of edges with very large radii and very small radii, creating a relatively standard styling appearance with no defining lean towards smooth or sharp."
    })
// Create an edge to connect up to the master node.
CREATE
    (EdginessTopNode)-[:INPUT]->(graphTopNode1),
    (edginess1)-[:INPUT]->(EdginessTopNode)


// Add Companies.
// DELETED FOR NOW - Add a top node for Companies which holds the required fields and data types.
// CREATE (CompanyTopNode:Company:TopNode {
//         // id: "integer",
// 		// To be completed later on if agreed we need it.
//     })
// Add all Companies.
CREATE (company1:Company {
        // id: 2,
        //cruser: 1, // Do we need this?
		name: "Ford",
        // title: "Ford", // Do we still need title? Same as name for all current cases.
		visualOrder: 16,
		// homeCountry: 1, // No longer needed as edge exists.
        // companyStructuredness: 1, // No longer needed as edge exists.
        // companySize: 3, // No longer needed as edge exists.
        // technologyLevel: 2, // No longer needed as edge exists.
        numberPastProjects: 50
        // crdate: "20/01/2020  14:10:00", // Will no longer need.
        // tstamp: "20/01/2020  14:10:00" // Will no longer need.
    }),
    (company2:Company {
        // id: 4,
        //cruser: 1, // Do we need this?
		name: "Rivian",
        // title: "Rivian", // Do we still need title? Same as name for all current cases.
		visualOrder: 9,
		// homeCountry: 1, // No longer needed as edge exists.
        // companyStructuredness: 1, // No longer needed as edge exists.
        // companySize: 3, // No longer needed as edge exists.
        // technologyLevel: 2, // No longer needed as edge exists.
        numberPastProjects: 1
        // crdate: "20/01/2020  14:10:00", // Will no longer need.
        // tstamp: "20/01/2020  14:10:00" // Will no longer need.
    })
// Create an edge to connect up to the master node.
CREATE
    // TopNode DELETED FOR NOW.
    // (CompanyTopNode)-[:INPUT]->(graphTopNode3),
    // (company1)-[:INPUT]->(CompanyTopNode)
    (company1)-[:INPUT]->(graphTopNode3),
    (company2)-[:INPUT]->(graphTopNode3)
// Create an edge to connect up the Company to the relevant Country node for Home Country.
CREATE
    (company1)-[:HAS_HOME_COUNTRY]->(country1),
    (company2)-[:HAS_HOME_COUNTRY]->(country1)
// Create an edge to connect up the Company to the relevant CompanySize node.
CREATE
    (company1)-[:HAS_SIZE]->(companySize1),
    (company2)-[:HAS_SIZE]->(companySize2)
// Create an edge to connect up the Company to the relevant CompanyStructuredness node.
CREATE
    (company1)-[:HAS_STRUCTUREDNESS]->(companyStructuredness1),
    (company2)-[:HAS_STRUCTUREDNESS]->(companyStructuredness2)
// Create an edge to connect up the Company to the relevant TechnologyLevel node.
CREATE
    (company1)-[:HAS_TECHNOLOGY_LEVEL]->(technologyLevel1),
    (company2)-[:HAS_TECHNOLOGY_LEVEL]->(technologyLevel1)


// Add Terms.
// Add a top node for Terms which holds the required fields and data types.
CREATE (TermTopNode:Term:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all Terms.
CREATE (term1:Term:Meta {
        // id: 130,
		shortTerm: "TH",
        longTerm: "Top Hat",
        modifiable: true, // Changed to true/false boolean
        displayInformation: "The top hat typically includes the components and systems that make up the upper half of the vehicle including the body work, closures and interior."
        // comment: "" // No longer needed? Or useful for somewhere for a user to leave some form of comment?
    })
// Create an edge to connect up to the master node.
CREATE
    (TermTopNode)-[:INPUT]->(graphTopNode1),
    (term1)-[:INPUT]->(TermTopNode)

// Add Company_Has_Term Edges.
CREATE
    (company1)-[:HAS_TERM {
        // id: 144,
        newShortTerm: "UP",
        newLongTerm: "Upperbody"
    }]->(term1),
    (company2)-[:HAS_TERM {
        // id: 145,
        newShortTerm: "TB",
        newLongTerm: "Topbody"
    }]->(term1)


// Add CompanyPlants.
// DELETED FOR NOW - Add a top node for CompanyPlants which holds the required fields and data types.
// CREATE (CompanyPlantTopNode:CompanyPlant:TopNode {
//         // id: "integer",
// 		// To be completed later on if agreed we need it.
//     })
// Add all CompanyPlants.
CREATE (companyPlant1:CompanyPlant {
        // id: 2,
        //company: 2, // No longer needed as edge exists.
        //country: 1, // No longer needed as edge exists.
		greenField: false, // Consider changing if we add a "browness" factor? Also better to be boolean.
        name: "KCAP", // Optional new field I propose we add to make a unique identifier for a company that might have two plants in say USA?
        facilityReadiness: 50.0,
        capacity: 350000.0,
        workingDaysPerWeek: 5.0,
        workingHoursPerDay: 16.0
        // workingHoursModel: 2 // No longer needed as edge exists.
    }),
    (companyPlant2:CompanyPlant {
        // id: 3,
        //company: 5, // No longer needed as edge exists.
        //country: 1, // No longer needed as edge exists.
		greenField: false, // Consider changing if we add a "browness" factor? Also better to be boolean.
        name: "Normal, Illinois", // Optional new field I propose we add to make a unique identifier for a company that might have two plants in say USA?
        facilityReadiness: 80.0,
        capacity: 120000.0,
        workingDaysPerWeek: 5.0,
        workingHoursPerDay: 16.0
        // workingHoursModel: 2 // No longer needed as edge exists.
    })
// Create an edge to connect up to the master node.
// TopNode DELETED FOR NOW.
// CREATE
    // (CompanyPlantTopNode)-[:INPUT]->(graphTopNode3),
    // (companyPlant1)-[:INPUT]->(CompanyPlantTopNode)
// Create an edge to connect up to the relevant Company node.
CREATE
    (company1)-[:HAS_PLANT]->(companyPlant1),
    (company2)-[:HAS_PLANT]->(companyPlant2)
// Create an edge to connect up the plant to the relevant country node.
CREATE
    (companyPlant1)-[:HAS_COUNTRY]->(country1),
    (companyPlant2)-[:HAS_COUNTRY]->(country1)
// Create an edge to connect up to the relevant WorkingHoursModel node.
CREATE
    (companyPlant1)-[:HAS_WORKING_HOUR_MODEL]->(workingHoursModel1),
    (companyPlant2)-[:HAS_WORKING_HOUR_MODEL]->(workingHoursModel1)


// Add DataChanged.
CREATE (dataChanged1:DataChanged:Meta {
		dataChanged: 0 // Discuss if needed. Helpful place to store if a user has made a change or now redundant? If needed, change to boolean.
	})
// Create an edge to connect up to the master node.
CREATE
    (dataChanged1)-[:INPUT]->(graphTopNode1)


// Add sdpVersion.
CREATE (sdpVersion1:Version:Meta {
		versionNumber: "4.0.0.0045.0890" // Need to rethink how/if we regenerate this in SDP4.
    })
// Create an edge to connect up to the master node.
CREATE
    (sdpVersion1)-[:INPUT]->(graphTopNode1)


// Add FunctionalSafetyCriticality.
// Add a top node for FunctionalSafetyCriticalities which holds the required fields and data types.
CREATE (FunctionalSafetyCriticalityTopNode:FunctionalSafetyCriticality:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all FunctionalSafetyCriticalities.
CREATE (functionalSafetyCriticality5:FunctionalSafetyCriticality:Meta {
        // id: 5,
		name: "{ASIL} D",
		value: 5.0,
		displayInformation: "Highest classification of initial hazard (injury risk) defined within {ISO} 26262."
    })
// Create an edge to connect up to the master node.
CREATE
    (FunctionalSafetyCriticalityTopNode)-[:INPUT]->(graphTopNode1),
    (functionalSafetyCriticality5)-[:INPUT]->(FunctionalSafetyCriticalityTopNode)


// Add Propulsion.
// Add a top node for Propulsions which holds the required fields and data types.
CREATE (PropulsionTopNode:Propulsion:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all Propulsions.
CREATE (propulsion2:Propulsion:Meta {
        // id: 2,
		name: "diesel",
        // displayName: "Diesel", // Delete and just use name? But capitalise first.
		displayInformation: "A compression-ignition engine vehicle run on diesel fuel"
    }),
    (propulsion6:Propulsion:Meta {
        // id: 6,
		name: "BEV",
        // displayName: "BEV",
		displayInformation: "A Battery Electric Vehicle with no secondary source of propulsion"
    })
// Create an edge to connect up to the master node.
CREATE
    (PropulsionTopNode)-[:INPUT]->(graphTopNode1),
    (propulsion2)-[:INPUT]->(PropulsionTopNode),
    (propulsion6)-[:INPUT]->(PropulsionTopNode)


// Add Newness.
// Add a top node for Newness' which holds the required fields and data types.
CREATE (NewnessTopNode:Newness:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all Newness'.
CREATE (newness1:Newness:Meta {
        // id: 1,
		name: "Carry over",
		value: 1.0,
		displayInformation: "The commodity is an existing concept and design and requires no integration work into the new vehicle."
    }),
    (newness2:Newness:Meta {
        // id: 2,
		name: "Minor changes",
		value: 2.0,
		displayInformation: "The commodity is an existing concept and design and only minor modifications and integration work is required to integrate it into the new vehicle."
    }),
    (newness3:Newness:Meta {
        // id: 3,
		name: "Moderate changes",
		value: 3.0,
		displayInformation: "The commodity is an existing concept but the design requires modifications and integration work to implement it into the new vehicle."
    }),
    (newness4:Newness:Meta {
        // id: 4,
		name: "Major changes",
		value: 4.0,
		displayInformation: "The commodity is an updated concept with little or no previous design work to develop on from."
    }),
    (newness5:Newness:Meta {
        // id: 5,
		name: "New",
		value: 5.0,
		displayInformation: "The commodity is a new concept with no previous design work to develop on from."
    }),
    (newness6:Newness:Meta {
        // id: 6,
		name: "N/A",
		value: 0.0,
		displayInformation: "The commodity is not required or not applicable to the product."
    })
// Create an edge to connect up to the master node.
CREATE
    (NewnessTopNode)-[:INPUT]->(graphTopNode1),
    (newness1)-[:INPUT]->(NewnessTopNode),
    (newness2)-[:INPUT]->(NewnessTopNode),
    (newness3)-[:INPUT]->(NewnessTopNode),
    (newness4)-[:INPUT]->(NewnessTopNode),
    (newness5)-[:INPUT]->(NewnessTopNode),
    (newness6)-[:INPUT]->(NewnessTopNode)


// Add Teams.
// Create "super" node for teams (from "connectteams.cypher").
// CREATE (TeamSuperNode:Team:Meta { // Discuss if still needed. Agreed to rename to be top node instead of super node.
    // name: "Team Super Node"
CREATE (TeamTopNode:Team:Meta:TopNode {
        // id:0 // Only needed in the old cypher?
        number: "TE000",
        cumulativeResourceDistribution: [], // Needed on TeamTopNode? // Moved to projectTeam node.
        cumulativeResourceDistributionRounded: [] // Needed on TeamTopNode? // Moved to projectTeam node.
    })
// Create team nodes for "?" and "-" (from "connectteams.cypher").
// CREATE (BlankTeam:Team:Meta { // Discuss if still needed. Agreed we no longer need "-" team anymore for new structure.
//         name: "-",
//         teamSkillLevel: 0.0
//     })
// Add level 1 and level 2 Teams.
CREATE (team8:Team:Level1Team:Meta {
        // id: 8,
        number: "TE008",
		name: "Styling Teams",
        // parent: NULL, // No longer needed as edge created?
        // swimlane: 3, // What do we do with this connection? For resource chart?
        visualOrder: 8,
		roleDescription: "All teams responsible for delivering the content outlined in the Styling swimlane" // Previously called "roledescription".
        
        // Properties from "connectteams.cypher".
        // cumulativeResourceDistribution: [], // Moved to projectTeam node.
        // cumulativeResourceDistributionRounded: [] // Moved to projectTeam node.
    }),
    (team10:Team:Level2Team:Meta {
        // id: 10,
        number: "TE010",
		name: "Designer",
        // parent: 8,
        // swimlane: 3,
        visualOrder: 10,
		roleDescription: "Responsible for producing the different style themes of the vehicle" // Previously called "roledescription".
    }),
    (team13:Team:Level2Team:Meta {
        // id: 13,
        number: "TE013",
		name: "Craftsmanship",
        // parent: 8,
        // swimlane: 3,
        visualOrder: 13,
		roleDescription: "Responsible for fit and finish of all vehicle surfaces" // Previously called "roledescription".
    })
// Create an edge to connect up to the master node.
CREATE
    (TeamTopNode)-[:INPUT]->(graphTopNode1)
// Create edges to connect up the teams.
CREATE
    (team8)-[:IS_SUBTEAM_OF]->(TeamTopNode),
    (team10)-[:IS_SUBTEAM_OF]->(team8),
    (team13)-[:IS_SUBTEAM_OF]->(team8)


// Add TechnologyComplexity.
// Add a top node for TechnologyComplexities which holds the required fields and data types.
CREATE (TechnologyComplexityTopNode:TechnologyComplexity:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all TechnologyComplexities.
CREATE (technologyComplexity4:TechnologyComplexity:Meta {
        // id: 3,
		name: "High",
		value: 4.0,
		displayInformation: "New technology development"
    })
// Create an edge to connect up to the master node.
CREATE
    (TechnologyComplexityTopNode)-[:INPUT]->(graphTopNode1),
    (technologyComplexity4)-[:INPUT]->(TechnologyComplexityTopNode)


// Add TrimLevel.
// Add a top node for TrimLevels which holds the required fields and data types.
CREATE (TrimLevelTopNode:TrimLevel:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all TrimLevels.
CREATE (trimLevel2:TrimLevel:Meta {
        // id: 2,
		name: "Medium",
		complexity: 0.333,
		displayInformation: "A medium trim level involves a developed exterior and interior trim design for the majority of market vehicles, balancing budget, comfort and style."
    })
// Create an edge to connect up to the master node.
CREATE
    (TrimLevelTopNode)-[:INPUT]->(graphTopNode1),
    (trimLevel2)-[:INPUT]->(TrimLevelTopNode)


// Add Projects.
// DELETED FOR NOW - Add a top node for Projects which holds the required fields and data types.
// CREATE (ProjectTopNode:Project:TopNode {
//         // id: "integer",
// 		// To be completed later on if agreed we need it.
//     })
// Add all Projects.
CREATE (project1:Project:FordFiesta3Bodystyles {
        // id: 1,
        // cruser: 1, // Do we need this?
        // company: 2, // No longer needed as edge added.
		name: "Ford Fiesta 3 Bodystyles",
        //leftDrive: true, // Removed as connected up to a new node "handDrive". Need to add option for noHandDrive soon (autonomous) and centralSteeringWheel (single seaters)?.
        //rightDrive: false, // See above comment.
        //frontWheelDrive: false, // Removed as connected to new node "wheelDrive". Or better to leave as three properties?
        //rearWheelDrive: true, // See above comment.
        //allWheelDrive: false, // See above comment.
        displaySoftware: true, // Discuss removing as we have Display Options tab now.
        displayPropulsion: true, // Discuss removing as we have Display Options tab now.
        virtualModelsSelected: false, // Discuss. Feels like we can do better and clearer.
        workingDaysPerWeek: 5,
        workingHoursPerDay: 8
        // workingHoursModel: 1, // Deleted as new edge exists below.
        // crdate: "20/01/2020  14:10:00", // Will no longer need.
        // tstamp: "20/01/2020  14:10:00" // Will no longer need.
    }),
    (project2:Project:RivianSUV {
        // id: 1,
        // cruser: 1,
        // company: 2,
		name: "Rivian SUV",
        //leftDrive: true,
        //rightDrive: false,
        //frontWheelDrive: false,
        //rearWheelDrive: true,
        //allWheelDrive: false,
        displaySoftware: true,
        displayPropulsion: false,
        virtualModelsSelected: true,
        workingDaysPerWeek: 5,
        workingHoursPerDay: 8
        // workingHoursModel: 1,
        // crdate: "20/01/2020  14:10:00",
        // tstamp: "20/01/2020  14:10:00"
    })
// Create an edge to connect up to the master node.
// TopNode DELETED FOR NOW.
// CREATE
//     (ProjectTopNode)-[:INPUT]->(graphTopNode3),
//     (project1)-[:INPUT]->(ProjectTopNode)
// Create an edge to connect up the Project to the relevant Company.
CREATE
    (company1)-[:HAS_PROJECT]->(project1),
    (company2)-[:HAS_PROJECT]->(project2)
// Create an edge to connect up the Project to the correct WorkingHoursModel.
CREATE
    (project1)-[:HAS_WORKING_HOUR_MODEL]->(workingHoursModel1),
    (project2)-[:HAS_WORKING_HOUR_MODEL]->(workingHoursModel1)
// Create an edge to connect up the Project to the correct Propulsion.
CREATE
    (project1)-[:HAS_PROPULSION {
        id: 1 // Delete long term but left in to mkae cypher script work.
        // project: 1, // No longer needed as this is the job of the edge?
        // propulsion: 2, // No longer needed as this is the job of the edge?
    }]->(propulsion2),
    (project2)-[:HAS_PROPULSION {
        id: 2 // Delete long term but left in to mkae cypher script work.
        // project: 1, // No longer needed as this is the job of the edge?
        // propulsion: 2, // No longer needed as this is the job of the edge?
    }]->(propulsion6)


// Added on 09/03/2022 after Master Graph Review.

// Add project variant nodes.
CREATE (projectVariant1:ProjectVariant:FordFiesta3Bodystyles {
		project: "Ford Fiesta 3 Bodystyles",
        name: "Plan A", // Variant name saved by User from UI.
        description: "Deleted all Mule build activities in line with customer request.", // Description saved from UI by User detailing variant.
        userActionsJSON:         
        '{  type: "resourcesChangedWithoutWorkloadChange",
            typeName: "resourcesChangedWithoutWorkloadChange",
            number: "EV0008",
            affectedEntityNumber: "A0009",
            valueOriginal: 9.0,
            valueNew: 6.0,
            valueUnit: "resources",
            timestamp: "2021-12-13T14:02:38.465000000",
            impact: "Activity resources changed. Duration adapted from 44 to 44 days.", 
            mostRecent: false,
            user: "lbangs"
        },
        {   type: "markedHidden",
            typeName: "markedHidden",
            number: "EV0026",
            affectedEntityNumber: "U0024",
            valueOriginal: "n/a",
            valueNew: "n/a",
            valueUnit: "n/a",
            timestamp: "2021-12-13T14:02:38.465000000",
            impact: "Tag hidden. All activities with tag hidden.", 
            mostRecent: false,
            user: "lbangs"
        }'
    }),
    (projectVariant2:ProjectVariant:RivianSUV {
		project: "Rivian SUV",
        name: "1st Draft",
        description: "Increased length of VP tooling.",
        userActionsJSON: "TBC"
    })

// Create an edge to connect up the Variant to the relevant Project.
CREATE
    (project1)-[:HAS_VARIANT]->(projectVariant1),
    (project2)-[:HAS_VARIANT]->(projectVariant2)


// Add Hand Drives.
// Add a top node for Hand Drives which holds the required fields and data types.
CREATE (HandDriveTopNode:HandDrive:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all Hand Drives.
CREATE (handDrive1:HandDrive:Meta {
        // id: 1,
        name: "LHD",
        description: "Left hand drive option."
    }),
    (handDrive2:HandDrive:Meta {
        // id: 2,
        name: "RHD",
        description: "Right hand drive option."
    })
// Create an edge to connect up to the master node.
CREATE
    (HandDriveTopNode)-[:INPUT]->(graphTopNode1),
    (handDrive1)-[:INPUT]->(HandDriveTopNode),
    (handDrive2)-[:INPUT]->(HandDriveTopNode)


// Add Wheel Drives.
// Add a top node for Wheel Drives which holds the required fields and data types.
CREATE (WheelDriveTopNode:WheelDrive:Meta:TopNode {
        // id: "integer",
		// To be completed later on if agreed we need it.
    })
// Add all Wheel Drives.
CREATE (wheelDrive1:WheelDrive:Meta {
        // id: 1,
        name: "FWD",
        description: "Front wheel drive option."
    }),
    (wheelDrive2:WheelDrive:Meta {
        // id: 2,
        name: "RWD",
        description: "Rear wheel drive option."
    }),
    (wheelDrive3:WheelDrive:Meta {
        // id: 3,
        name: "AWD",
        description: "All wheel drive option."
    })
// Create an edge to connect up to the master node.
CREATE
    (WheelDriveTopNode)-[:INPUT]->(graphTopNode1),
    (wheelDrive1)-[:INPUT]->(WheelDriveTopNode),
    (wheelDrive2)-[:INPUT]->(WheelDriveTopNode),
    (wheelDrive3)-[:INPUT]->(WheelDriveTopNode)


// Add ProjectBodystyles.
// DELETED FOR NOW - Add a top node for ProjectBodystyles which holds the required fields and data types.
// CREATE (ProjectBodystyleTopNode:ProjectBodystyle:TopNode {
//         // id: "integer",
// 		// To be completed later on if agreed we need it.
//     })
// Add all ProjectBodystyles.
CREATE (projectBodystyle1:ProjectBodystyle:FordFiesta3Bodystyles {
        // id: 2,
        // project: 1, // No longer needed as we have an edge.
        // bodystyle: 5, // No longer needed as we have an edge.
        // edginess: 2, // No longer needed as we have an edge.
        volume: NULL, // What is this currently used for?
        stylingImportance: 50
    }),
    (projectBodystyle2:ProjectBodystyle:RivianSUV {
        // id: 3,
        // project: 3,
        // bodystyle: 2,
        // edginess: 3,
        volume: NULL,
        stylingImportance: 60
    })
// Create an edge to connect up to the master node.
// TopNode DELETED FOR NOW.
// CREATE
//     (ProjectBodystyleTopNode)-[:INPUT]->(graphTopNode3),
//     (projectBodystyle1)-[:INPUT]->(ProjectBodystyleTopNode)
// Create an edge to connect up the Project to the correct ProjectBodystyle.
CREATE
    (project1)-[:HAS_PROJECTBODYSTYLE]->(projectBodystyle1),
    (project2)-[:HAS_PROJECTBODYSTYLE]->(projectBodystyle2)
// Create an edge to connect up the ProjectBodystyle to the correct Bodystyle.
CREATE
    (projectBodystyle1)-[:HAS_BODYSTYLE]->(bodystyle1),
    (projectBodystyle2)-[:HAS_BODYSTYLE]->(bodystyle6)
// Create an edge to connect up the ProjectBodystyle to the correct Edginess.
CREATE
    (projectBodystyle1)-[:HAS_STYLING_COMPLEXITY]->(edginess1), // Previously called "HAS_EDGINESS".
    (projectBodystyle2)-[:HAS_STYLING_COMPLEXITY]->(edginess1)
// Create an edge to connect up the ProjectBodystyle to the correct CompanyPlant.
CREATE
    (projectBodystyle1)-[:IS_PRODUCED_IN { // Previously called "HAS_COMPANYPLANT".
        id: 2 // Delete long term but left in to mkae cypher script work.
        // projectBodyStyle: 2, // No longer needed as this is the job of the edge?
        // companyPlant: 2 // No longer needed as this is the job of the edge?
    }]->(companyPlant1),
    (projectBodystyle2)-[:IS_PRODUCED_IN {
        id: 3
        // projectBodyStyle: 2,
        // companyPlant: 2
    }]->(companyPlant2)
// Create an edge to connect up the ProjectBodystyle to the correct Country.
CREATE
    (projectBodystyle1)-[:IS_SOLD_IN { // Previously called "HAS_COUNTRY".
        id: 4 // Delete long term but left in to mkae cypher script work.
        // projectBodyStyle: 2, // No longer needed as this is the job of the edge?
        // country: 1 // No longer needed as this is the job of the edge?
    }]->(country1),
    (projectBodystyle2)-[:IS_SOLD_IN {
        id: 5
        // projectBodyStyle: 2,
        // country: 1
    }]->(country1),
    (projectBodystyle2)-[:IS_SOLD_IN {
        id: 6
        // projectBodyStyle: 2,
        // country: 1
    }]->(country2)
// Create an edge to connect up the ProjectBodystyle to the correct HandDrive.
CREATE
    (projectBodystyle1)-[:HAS_HAND_DRIVE]->(handDrive1),
    (projectBodystyle2)-[:HAS_HAND_DRIVE]->(handDrive1)
// Create an edge to connect up the ProjectBodystyle to the correct WheelDrive.
CREATE
    (projectBodystyle1)-[:HAS_WHEEL_DRIVE]->(wheelDrive2),
    (projectBodystyle2)-[:HAS_WHEEL_DRIVE]->(wheelDrive3)
// Create an edge to connect up the ProjectBodystyle to the correct TrimLevel.
CREATE
    (projectBodystyle1)-[:HAS_TRIMLEVEL {
        id: 2 // Delete long term but left in to mkae cypher script work.
        // projectBodyStyle: 2, // No longer needed as this is the job of the edge?
        // trimLevel: 1 // No longer needed as this is the job of the edge?
    }]->(trimLevel2),
    (projectBodystyle2)-[:HAS_TRIMLEVEL {
        id: 2
        // projectBodyStyle: 2,
        // trimLevel: 1
    }]->(trimLevel2)


// Add ProjectCommodity.
// DELETED FOR NOW - Add a top node for ProjectCommodities which holds the required fields and data types.
// CREATE (ProjectCommodityTopNode:ProjectCommodity:TopNode {
//         // id: "integer",
// 		// To be completed later on if agreed we need it.
//     })
// Add all ProjectCommodities.
CREATE (projectCommodity1:ProjectCommodity:Level1Commodity:FordFiesta3Bodystyles {
        id: 1,
        // project: 1, // No longer needed as we have an edge.
        // commodity: 1, // No longer needed as we have an edge.
        // newness: 1, // No longer needed as we have an edge.
        supplierExists: true, // Changed to true/false boolean
        name: "Ford - Platform" // Delete when done with property. Would it make sense to add some form of name to this node, possibly a concat of the project and commodity? Otherwise its hard to identify the node.
    }),
    (projectCommodity2:ProjectCommodity:Level2Commodity:FordFiesta3Bodystyles {
        id: 2,
        // project: 1,
        // commodity: 4,
        // newness: 1,
        supplierExists: true,
        name: "Ford - Front Structure" // Delete when done with property.
    }),
    (projectCommodity3:ProjectCommodity:Level3Commodity:FordFiesta3Bodystyles {
        id: 3,
        // project: 1,
        // commodity: 5,
        // newness: 6,
        supplierExists: false,
        name: "Ford - Front Beam" // Delete when done with property.
    }),
    (projectCommodity4:ProjectCommodity:Level3Commodity:FordFiesta3Bodystyles {
        id: 4,
        // project: 1,
        // commodity: 6,
        // newness: 1,
        supplierExists: true,
        name: "Ford - Shotgun Beams" // Delete when done with property.
    }),
    (projectCommodity5:ProjectCommodity:Level1Commodity:RivianSUV {
        id: 5,
        // project: 2,
        // commodity: 1,
        // newness: 1,
        supplierExists: true,
        name: "Rivian - Platform" // Delete when done with property.
    }),
    (projectCommodity6:ProjectCommodity:Level2Commodity:RivianSUV {
        id: 6,
        // project: 2,
        // commodity: 4,
        // newness: 1,
        supplierExists: true,
        name: "Rivian - Front Structure" // Delete when done with property.
    }),
    (projectCommodity7:ProjectCommodity:Level3Commodity:RivianSUV {
        id: 7,
        // project: 2,
        // commodity: 5,
        // newness: 6,
        supplierExists: false,
        name: "Rivian - Front Beam" // Delete when done with property.
    }),
    (projectCommodity8:ProjectCommodity:Level3Commodity:RivianSUV {
        id: 8,
        // project: 2,
        // commodity: 6,
        // newness: 1,
        supplierExists: true,
        name: "Rivian - Shotgun Beams" // Delete when done with property.
    })
// Create an edge to connect up to the master node.
// TopNode DELETED FOR NOW.
// CREATE
    // (ProjectCommodityTopNode)-[:INPUT]->(graphTopNode3),
    // (projectCommodity1)-[:INPUT]->(ProjectCommodityTopNode),
    // (projectCommodity2)-[:INPUT]->(ProjectCommodityTopNode),
    // (projectCommodity3)-[:INPUT]->(ProjectCommodityTopNode),
    // (projectCommodity4)-[:INPUT]->(ProjectCommodityTopNode)

// Create an edge to connect up the ProjectCommodity to its parent ProjectCommodity to replicate the structure of Commodities.
CREATE
    (projectCommodity2)-[:SUB_COMMODITY_OF]->(projectCommodity1),
    (projectCommodity3)-[:SUB_COMMODITY_OF]->(projectCommodity2),
    (projectCommodity4)-[:SUB_COMMODITY_OF]->(projectCommodity2),
    (projectCommodity6)-[:SUB_COMMODITY_OF]->(projectCommodity5),
    (projectCommodity7)-[:SUB_COMMODITY_OF]->(projectCommodity6),
    (projectCommodity8)-[:SUB_COMMODITY_OF]->(projectCommodity6)

// Create an edge to connect up the Project to the correct ProjectCommodity.
CREATE
    (project1)-[:HAS_PROJECTCOMMODITY]->(projectCommodity1),
    (project1)-[:HAS_PROJECTCOMMODITY]->(projectCommodity2),
    (project1)-[:HAS_PROJECTCOMMODITY]->(projectCommodity3),
    (project1)-[:HAS_PROJECTCOMMODITY]->(projectCommodity4),
    (project2)-[:HAS_PROJECTCOMMODITY]->(projectCommodity5),
    (project2)-[:HAS_PROJECTCOMMODITY]->(projectCommodity6),
    (project2)-[:HAS_PROJECTCOMMODITY]->(projectCommodity7),
    (project2)-[:HAS_PROJECTCOMMODITY]->(projectCommodity8)
// Create an edge to connect up the ProjectCommodity to the correct Commodity.
CREATE
    (projectCommodity1)-[:HAS_COMMODITY]->(commodity1),
    (projectCommodity2)-[:HAS_COMMODITY]->(commodity4),
    (projectCommodity3)-[:HAS_COMMODITY]->(commodity5),
    (projectCommodity4)-[:HAS_COMMODITY]->(commodity6),
    (projectCommodity5)-[:HAS_COMMODITY]->(commodity1),
    (projectCommodity6)-[:HAS_COMMODITY]->(commodity4),
    (projectCommodity7)-[:HAS_COMMODITY]->(commodity5),
    (projectCommodity8)-[:HAS_COMMODITY]->(commodity6)
// Create an edge to connect up the ProjectCommodity to the correct Newness.
CREATE
    (projectCommodity1)-[:HAS_NEWNESS]->(newness3),
    (projectCommodity2)-[:HAS_NEWNESS]->(newness2),
    (projectCommodity3)-[:HAS_NEWNESS]->(newness6),
    (projectCommodity4)-[:HAS_NEWNESS]->(newness1),
    (projectCommodity5)-[:HAS_NEWNESS]->(newness2),
    (projectCommodity6)-[:HAS_NEWNESS]->(newness5),
    (projectCommodity7)-[:HAS_NEWNESS]->(newness6),
    (projectCommodity8)-[:HAS_NEWNESS]->(newness4)


// Add ProjectTeam.
// DELETED FOR NOW - Add a top node for ProjectTeams which holds the required fields and data types.
// CREATE (ProjectTeamTopNode:ProjectTeam:TopNode {
//         // id: "integer",
// 		// To be completed later on if agreed we need it.
//     })
// Add all ProjectTeams.
CREATE (projectTeam1:ProjectTeam:Level1Team:FordFiesta3Bodystyles {
        // id: 1,
        // project: 1, // No longer needed as we have an edge.
        // team: 1, // No longer needed as we have an edge.
        skillLevel: 70,
        name: "Ford Styling Teams", // Delete when done with property. Would it make sense to add some form of name to this node, possibly a concat of the project and team? Otherwise its hard to identify the node.
        // Properties from "connectteams.cypher".
        cumulativeResourceDistribution: [],
        cumulativeResourceDistributionRounded: []
    }),
    (projectTeam2:ProjectTeam:Level2Team:FordFiesta3Bodystyles {
        // id: 10,
        // project: 1,
        // team: 10,
        skillLevel: 70,
        name: "Ford Designer", // Delete when done with property.
        resourceDistribution: [],
        resourceDistributionRounded: []
    }),
    (projectTeam3:ProjectTeam:Level1Team:RivianSUV {
        // id: 1,
        // project: 1, // No longer needed as we have an edge.
        // team: 1, // No longer needed as we have an edge.
        skillLevel: 60,
        name: "Rivian Styling Teams", // Delete when done with property. Would it make sense to add some form of name to this node, possibly a concat of the project and team? Otherwise its hard to identify the node.
        cumulativeResourceDistribution: [],
        cumulativeResourceDistributionRounded: []
    }),
    (projectTeam4:ProjectTeam:Level2Team:RivianSUV {
        // id: 11,
        // project: 2,
        // team: 10,
        skillLevel: 60,
        name: "Rivian Designer", // Delete when done with property.
        resourceDistribution: [],
        resourceDistributionRounded: []
    })
// Create an edge to connect up to the master node.
// TopNode DELETED FOR NOW.
// CREATE
//     (ProjectTeamTopNode)-[:INPUT]->(graphTopNode1),
//     (projectTeam10)-[:INPUT]->(ProjectTeamTopNode)
// Create an edge to connect up the ProjectTeams to ProjectTeams to copy the same structure as teams.
CREATE
    (projectTeam2)-[:IS_SUBTEAM_OF]->(projectTeam1),
    (projectTeam4)-[:IS_SUBTEAM_OF]->(projectTeam3)
// Create an edge to connect up the Project to the correct ProjectTeam.
CREATE
    (project1)-[:HAS_PROJECTTEAM]->(projectTeam1),
    (project1)-[:HAS_PROJECTTEAM]->(projectTeam2),
    (project2)-[:HAS_PROJECTTEAM]->(projectTeam3),
    (project2)-[:HAS_PROJECTTEAM]->(projectTeam4)
// Create an edge to connect up the ProjectTeam to the correct Team.
CREATE
    (projectTeam1)-[:HAS_TEAM]->(team8),
    (projectTeam2)-[:HAS_TEAM]->(team10),
    (projectTeam3)-[:HAS_TEAM]->(team8),
    (projectTeam4)-[:HAS_TEAM]->(team10)









// Master Run Graph Data

// We can create the Start and End Node as part of the Master Run Graph.
CREATE (StartNode:Milestone:End {
        // earliestFinish: 11.0,
        // earliestStart: 1.0,
        // endDay: 11.0,
        isCritical: true,
        // latestFinish: 11.0,
        // latestStart: 1.0,
        // maturity: 1.0,
        mightBeCritical: true,
        // milestoneOrder: 0,
        name: "Start",
        number: "Start"
        // percentageComplete: 1.0,
        // startDay: 1.0
    }),
    (EndNode:Milestone:End {
        // earliestFinish: 11.0,
        // earliestStart: 1.0,
        // endDay: 11.0,
        isCritical: true,
        // latestFinish: 11.0,
        // latestStart: 1.0,
        // maturity: 1.0,
        mightBeCritical: true,
        // milestoneOrder: 0,
        name: "Finish",
        number: "Finish"
        // percentageComplete: 1.0,
        // startDay: 1.0
    })
// Create an edge to connect up the start node to the master node.
CREATE
    (StartNode)-[:INPUT]->(graphTopNode2),
    (EndNode)-[:INPUT]->(graphTopNode2)


// Create Swimlanes.
CREATE
    (swimlane1:Swimlane:MS {
        // id: 1,
        number: "S0001",
        // title: "Milestones", // Propose we delete as no longer needed?
        name: "Milestones",
        label: "MS",
        description: "The Milestones swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Milestones activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 1,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",

        // Properties from "cypher/dbinit/initialsetup/connectswimlanesandactivities.cypher".
        taskStarts: [], // What does this do?
		display: true,
		delete: false,
		swimlaneName: "Milestones", // s.name. Needed again? Or can we just use name?
		swimlaneNumber: "S0001", // s.number. Needed again? Or can we just use number?
		sdpName: "Milestones", // s.title. Use the value from "name" instead of from the proposed deleted "title".
		companyName: "Milestones" // s.title. Use the value from "name" instead of from the proposed deleted "title".
	}),
    (swimlane2:Swimlane:PR {
        number: "S0002",
        name: "Program",
        label: "PR",
        description: "The Program swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Styling activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 3,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",
        taskStarts: [],
		display: true,
		delete: false,
		swimlaneName: "Program",
		swimlaneNumber: "S0002",
		sdpName: "Program",
		companyName: "Program"
	}),
    (swimlane3:Swimlane:SY {
        // id: 3,
        number: "S0003",
        // title: "Styling", // Propose we delete as no longer needed?
        name: "Styling",
        label: "SY",
        description: "The Styling swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Styling activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 3,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",

        // Properties from "cypher/dbinit/initialsetup/connectswimlanesandactivities.cypher".
        taskStarts: [], // What does this do?
		display: true,
		delete: false,
		swimlaneName: "Styling", // s.name. Needed again? Or can we just use name?
		swimlaneNumber: "S0003", // s.number. Needed again? Or can we just use number?
		sdpName: "Styling", // s.title. Use the value from "name" instead of from the proposed deleted "title".
		companyName: "Styling" // s.title. Use the value from "name" instead of from the proposed deleted "title".
	}),
    (swimlane4:Swimlane:MD {
        number: "S0004",
        name: "Engineering Design",
        label: "MD",
        description: "The Engineering Design swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Engineering Design activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 4,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",
        taskStarts: [],
		display: true,
		delete: false,
		swimlaneName: "Engineering Design",
		swimlaneNumber: "S0004",
		sdpName: "Engineering Design",
		companyName: "Engineering Design"
	}),
    (swimlane5:Swimlane:PV {
        number: "S0005",
        name: "Physical Validation",
        label: "PV",
        description: "The Physical Validation swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Physical Validation activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 5,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",
        taskStarts: [],
		display: true,
		delete: false,
		swimlaneName: "Physical Validation",
		swimlaneNumber: "S0005",
		sdpName: "Physical Validation",
		companyName: "Physical Validation"
	}),
    (swimlane6:Swimlane:VD {
        number: "S0006",
        name: "Virtual Development",
        label: "VD",
        description: "The Virtual Development swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Virtual Development activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 6,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",
        taskStarts: [],
		display: true,
		delete: false,
		swimlaneName: "Virtual Development",
		swimlaneNumber: "S0005",
		sdpName: "Virtual Development",
		companyName: "Virtual Development"
	}),
    (swimlane7:Swimlane:SC {
        number: "S0007",
        name: "Supply Chain",
        label: "SC",
        description: "The Supply Chain swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Supply Chain activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 7,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",
        taskStarts: [],
		display: true,
		delete: false,
		swimlaneName: "Supply Chain",
		swimlaneNumber: "S0007",
		sdpName: "Supply Chain",
		companyName: "Supply Chain"
	}),
    (swimlane8:Swimlane:MF {
        number: "S0008",
        name: "Manufacturing",
        label: "MF",
        description: "The Manufacturing swimlane covers the design and development of the vehicle style and design for both exterior and interior surfaces",
        tagDescription: "All Manufacturing activities for designing and detailing the appearance of the vehicle.",
        visualOrder: 8,
        taskFillHex: "#FFFF00",
        taskBorderHex: "#D7D200",
        taskTextColourHex: "#000000",
        swimlaneBackgroundHex: "#FFFFE6",
        preProjectTaskFillHex: "#FFFFC8",
        preProjectTaskTextColourHex: "#000000",
        criticalPathTaskFillHex: "#807D00",
        criticalPathTaskTextColourHex: "#FFFFFF",
        taskStarts: [],
		display: true,
		delete: false,
		swimlaneName: "Manufacturing",
		swimlaneNumber: "S0008",
		sdpName: "Manufacturing",
		companyName: "Manufacturing"
	})
// Create an edge to connect up to the master node.
CREATE
    (swimlane1)-[:INPUT]->(graphTopNode2),
    (swimlane2)-[:INPUT]->(graphTopNode2),
    (swimlane3)-[:INPUT]->(graphTopNode2),
    (swimlane4)-[:INPUT]->(graphTopNode2),
    (swimlane5)-[:INPUT]->(graphTopNode2),
    (swimlane6)-[:INPUT]->(graphTopNode2),
    (swimlane7)-[:INPUT]->(graphTopNode2),
    (swimlane8)-[:INPUT]->(graphTopNode2)


// Connect level 1 Teams to Swimlanes.
CREATE 
    (team8)-[:BELONGS_TO_SWIMLANE]->(swimlane3)


// Create Milestones.
CREATE 
    (milestone1:Milestone {
        // id: 2,
        number: "M0002",
        name: "{CA}",
        // task: 118, // Do we even need to link Milestones to Tasks in the new set up? I think its an unneeded legacy connection for fast implementation.
        // longName: "{Concept Approval}",
        description: "Vehicle concept work is completed and approved by all stakeholders.",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Program Management",
        supportTeam: "-",
        // minPercentages: [1, 0], // See comment at top about stripping back all detail from Milestones.
        maxPercentages: [1, 0],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [0],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0,
        maxNumberPeople: 0,
        minTimeNeeded: 0,
        maxTimeNeeded: 0,
        unitTime: "weeks",
        degreeOfParallelization: 0,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "You will not have a defined {Concept Approval} point in the project when all teams have completed concept work and have moved to the next stage of the development process.",
        // company: 1, // Needed?
       	active: true, // Changed to true/false boolean
        testing: true // Changed to true/false boolean
        // timeChanged: "15/05/2020  12:07:52", // Agree to remove and replace with an automated timestamp?
        // comment: "Added following 03/03/2020 Workshop review with Mike." // Agree to remove?
	}),
    // Added more milestones for Event actions on 22/03/2022.
    (milestone2:Milestone {
        number: "M0009",
        name: "{PPSM}",
        description: "Pre Project Start Milestone",
        leadTeam: "Program Management",
        supportTeam: "-",
        maxPercentages: [1, 0],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [0],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0,
        maxNumberPeople: 0,
        minTimeNeeded: 0,
        maxTimeNeeded: 0,
        unitTime: "weeks",
        degreeOfParallelization: 0,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "You will not have a defined {Pre Project Start Milestone} point in the project when all teams have completed concept work and have moved to the next stage of the development process.",
        active: true,
        testing: true
	}),
    (milestone3:Milestone {
        number: "M0001",
        name: "{PS}",
        description: "Project Start",
        leadTeam: "Program Management",
        supportTeam: "-",
        maxPercentages: [1, 0],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [0],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0,
        maxNumberPeople: 0,
        minTimeNeeded: 0,
        maxTimeNeeded: 0,
        unitTime: "weeks",
        degreeOfParallelization: 0,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "You will not have a defined {Project Start} point in the project when all teams have completed concept work and have moved to the next stage of the development process.",
        active: true,
        testing: true
	})
// Connect up the Milestones to their corresponding Swimlane.
CREATE
    (milestone1)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane1),
    (milestone2)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane1),
    (milestone3)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane1)


// Create Tasks.
CREATE 
    (task1:Task:SY {
        // id: 12,
        number: "T0012", // Renamed from taskNumber.
        name: "Colour and Material Theme Development",
        // swimlane: 3, // Removed and edge added.
        longName: "o", // Renamed from taskLongName.
        description: "The ongoing development of colour and material choices with release drops happening throughout.", // Renamed from taskDescription.
        isPreProjectTask: false, // Changed to true/false boolean

        // Properties from "cypher/dbinit/initialsetup/connectswimlanesandactivities.cypher".
        swimlaneName: "Styling", // s.name. Do we need this if the below edge exists.
		swimlaneNumber: "S0003", // s.number. Do we need this if the below edge exists.
		swimlaneTitle: "Styling", // s.title. Do we need this if removing the property "title"?
		needed: true
	}),
    (task2:Task:PR {
        // id: 3,
        number: "T0003", // Renamed from taskNumber.
        name: "Ongoing Program Timing",
        // swimlane: 2, // Removed and edge added.
        longName: "o", // Renamed from taskLongName.
        description: "The ongoing tracking of costs and timing.", // Renamed from taskDescription.
        isPreProjectTask: false, // Changed to true/false boolean

        // Properties from "cypher/dbinit/initialsetup/connectswimlanesandactivities.cypher".
        swimlaneName: "Program", // s.name. Do we need this if the below edge exists.
		swimlaneNumber: "S0002", // s.number. Do we need this if the below edge exists.
		swimlaneTitle: "Program", // s.title. Do we need this if removing the property "title"?
		needed: true
	}),
    // New tasks added on 22/03/2022 for event nodes.
    (task3:Task:PR {
        number: "T0003",
        name: "Business Case Refinement",
        longName: "o",
        description: "o",
        isPreProjectTask: false,
        swimlaneName: "Program",
		swimlaneNumber: "S0002",
		swimlaneTitle: "Program",
		needed: true
	}),
    (task4:Task:PR {
        number: "T0004",
        name: "Media Event Planning and Execution",
        longName: "o",
        description: "o",
        isPreProjectTask: false,
        swimlaneName: "Program",
		swimlaneNumber: "S0002",
		swimlaneTitle: "Program",
		needed: true
	}),
    (task5:Task:PV {
        number: "T0005",
        name: "{EP} Build and Testing",
        longName: "o",
        description: "o",
        isPreProjectTask: false,
        swimlaneName: "Physical Validation",
		swimlaneNumber: "S0005",
		swimlaneTitle: "Physical Validation",
		needed: true
	}),
    (task6:Task:VD {
        number: "T0006",
        name: "Correlation With {EP} Analysis",
        longName: "o",
        description: "o",
        isPreProjectTask: false,
        swimlaneName: "Virtual Development",
		swimlaneNumber: "S0006",
		swimlaneTitle: "Virtual Development",
		needed: true
	}),
    (task7:Task:SC {
        number: "T0007",
        name: "Sourcing and Supplier Strategy Development",
        longName: "o",
        description: "o",
        isPreProjectTask: false,
        swimlaneName: "Supply Chain",
		swimlaneNumber: "S0007",
		swimlaneTitle: "Supply Chain",
		needed: true
	}),
    (task8:Task:PR {
        number: "T0008",
        name: "PPS Program Planning",
        longName: "o",
        description: "o",
        isPreProjectTask: true,
        swimlaneName: "Program",
		swimlaneNumber: "S0002",
		swimlaneTitle: "Program",
		needed: true
	}),
    (task9:Task:MD {
        number: "T0009",
        name: "Engineering Design",
        longName: "o",
        description: "o",
        isPreProjectTask: true,
        swimlaneName: "Engineering Design",
		swimlaneNumber: "S0004",
		swimlaneTitle: "Engineering Design",
		needed: true
	}),
    (task10:Task:MF {
        number: "T0010",
        name: "Manufacturing",
        longName: "o",
        description: "o",
        isPreProjectTask: true,
        swimlaneName: "Manufacturing",
		swimlaneNumber: "S0008",
		swimlaneTitle: "Manufacturing",
		needed: true
	})


// Connect up the Task to its corresponding Swimlane.
CREATE
    (task1)-[:BELONGS_TO_SWIMLANE {
        // swimlaneNumber: 3, // No longer needed as this is the job of the edge?
        // swimlaneTitle: "Styling", // No longer needed.
        needed: true
    }]->(swimlane3),
    (task2)-[:BELONGS_TO_SWIMLANE {
        // swimlaneNumber: 2, // No longer needed as this is the job of the edge?
        // swimlaneTitle: "Program", // No longer needed.
        needed: true
    }]->(swimlane2),
    (task3)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane2),
    (task4)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane2),
    (task5)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane5),
    (task6)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane6),
    (task7)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane7),
    (task8)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane2),
    (task9)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane4),
    (task10)-[:BELONGS_TO_SWIMLANE {
        needed: true
    }]->(swimlane8)


// Create Activities.
CREATE 
    (activity1:Activity:PR {
        // id: 1,
        number: "A0001",
        name: "Identify Markets",
        // task: 2,
        // longName: "o",
        description: "Identification of target markets applicable to this program. Review of market factors that will drive significant sales for the given product.",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Sales & Marketing",
        supportTeam: "Product Planning & Strategy",
        minPercentages: [1, 1],
        maxPercentages: [1, 1],
        outputCalc: "numberMarkets",
        functionType: "sigmoid",
        dependentOn: "initialNumberMarkets",
        weights: [1],
        minUnit: [1],
        maxUnit: [198],
        minNumberPeople: 5.00,
        maxNumberPeople: 15.00,
        minTimeNeeded: 2.00,
        maxTimeNeeded: 20.00,
        unitTime: "weeks",
        degreeOfParallelization: 1.00,
        additionalTimeFactor: "o", // Should we scrap this? How is it used/is it used well?
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "You will not have sufficient information regarding potentials or uncertainties in your targeted markets.",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: true, // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:20:57", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data" // Agree to remove?

        // Properties from "cypher/dbinit/initialsetup/connectswimlanesandactivities.cypher".
        task: "Ongoing Program Timing", // t.name. Do we need this if the activity is connected up to a task already?
		taskLongName: "Ongoing Program Timing", // t.longName. See above comment.
        taskNumber: "T0002", // t.number. See above comment.
		swimlaneId: 3, // t.swimlaneId. Do we need this if the activity is connected up to a task and thus swimlane already?
		swimlaneName: "Styling", // t.swimlaneName. See above comment.
		swimlaneNumber: "S0003", // t.swimlaneNumber. See above comment.
		swimlaneTitle: "Styling", // t.swimlaneTitle. See above comment.

        // Add the below properties in (from setupactivities.cypher)? Could make them non-modifiable?
        minPercFloat: [],
		maxPercFloat: [],
		active: false,
		done: false,
		daysWorked: 0.0,
		numberPredecessorsFinished: 0.0,
		inputValues: [],
		original: true,
		needed: true,
		group: "none",
		sumMinPerc: 0.0,
		sumMaxPerc: 0.0,
		minPercFloatWeighted: [],
		maxPercFloatWeighted: [],
		peopleStructureNumbers: [],
		peopleSkillLevels: [],
		peopleStructureNumbersSkills: [],
		// peopleStructure: a.lead + a.support, // This property still needs to be dynamically set?
		// originalName: a.name, // This property still needs to be dynamically set?
		// originalNumber: a.number, // This property still needs to be dynamically set?
		startingActivity: false,
		workedInTimeStepArray: [],
		currentStepPercentageArray: []
	}),
    (activity2:Activity:PV {
        // id: 52,
        number: "A0063",
        name: "Assess New Package Requirements",
        // task: 19,
        // longName: "o",
        description: "Engineering functional groups and safety, review and discuss the products new package requirements.",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "You will not have a first packaging concept which is required to support engineering and supply chain and manufacturing.",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity3:Activity:SY {
        // id: 52,
        number: "A0003",
        name: "Styling Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: 1,
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity4:Activity:MD {
        // id: 52,
        number: "A0004",
        name: "Engineering Design Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity5:Activity:ED {
        // id: 52,
        number: "A0005",
        name: "Software Development Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity6:Activity:IC {
        // id: 52,
        number: "A0006",
        name: "ICE Propulsion Development Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: 1,
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity7:Activity:HY {
        // id: 52,
        number: "A0007",
        name: "Hybrid Propulsion Development Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: 1,
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity8:Activity:B3 {
        // id: 52,
        number: "A0008",
        name: "BEV Milestones Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "numberMarkets",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: 1,
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity9:Activity:BE {
        // id: 52,
        number: "A0009",
        name: "BEV Propulsion Development Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity10:Activity:VD {
        // id: 52,
        number: "A0010",
        name: "Virtual Development Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity11:Activity:PV {
        // id: 52,
        number: "A0011",
        name: "Physical Validation Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity12:Activity:SC {
        // id: 52,
        number: "A0012",
        name: "Supply Chain Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity13:Activity:MF {
        // id: 52,
        number: "A0013",
        name: "Manufacturing Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "numberMarkets",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity14:Activity:AS {
        // id: 52,
        number: "A0014",
        name: "Serviceability, Service, After Sales Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity15:Activity:ST {
        // id: 52,
        number: "A0015",
        name: "Support Teams Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    (activity16:Activity:NA {
        // id: 52,
        number: "A0016",
        name: "- Activity",
        // task: 19,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
    }),
    // New activities added on 22/03/2022 to work with event nodes. 
    (activity17:Activity:PR {
        number: "A0036",
        name: "Create Initial Investment Numbers",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity18:Activity:PR {
        number: "A0957",
        name: "Release Initial Vehicle BoM",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity19:Activity:PR {
        number: "A0385",
        name: "Deliver Media Vehicles",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity20:Activity:PR {
        number: "A0546",
        name: "Confirm Business Case",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity21:Activity:PV {
        number: "A0394",
        name: "Finalise Test & Build Plan ({EP})",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity22:Activity:PR {
        number: "A0037",
        name: "Refine Business Case",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity23:Activity:VD {
        number: "A0381",
        name: "Prepare {EP} {CAE} Report",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity24:Activity:PR {
        number: "A0031",
        name: "Generate Initial Vehicle BoM Based On Engineering Experience",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity25:Activity:SC {
        number: "A0416",
        name: "Generate Commodity Business Plan",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    }), 
    (activity26:Activity:PR {
        number: "A0414",
        name: "Plan and Agree Project Setup",
        description: "o",
        leadTeam: "Package",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Propulsion;Electronics;Safety;Autonomous Vehicle Systems;Vehicle Engineering",
        minPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        maxPercentages: [0, 4, 4, 2, 2, 4, 2, 1, 3],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 20.00,
        maxNumberPeople: 20.00,
        minTimeNeeded: 3.00,
        maxTimeNeeded: 3.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.50,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "o",
        active: true,
        testing: false
    })

// Create some auxiliary nodes.
CREATE 
	(multStart1:MULTSTART:Activity:AuxiliaryNode {
		// id: 8,
        number: "A0009",
        name: "MultiplicationPointStart1",
        // task: 10,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "-",
        minPercentages: [0, 0],
        maxPercentages: [0, 0],
        outputCalc: "multiply",
        functionType: "const",
        dependentOn: "numberRegions",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0.00,
        maxNumberPeople: 0.00,
        minTimeNeeded: 0.00,
        maxTimeNeeded: 0.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.00,
        additionalTimeFactor: "o",
        multiplicationStrand: 1,
        loopStrand: "o", // Do we need all the non used properties on these aux nodes?
        decisionStrand: "o",
        risks: "x",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
	}),
    (multEnd1:MULTEND:Activity:AuxiliaryNode {
		// id: 9,
        number: "A0010",
        name: "MultiplicationPointEnd1",
        // task: 10,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "-",
        minPercentages: [0, 0],
        maxPercentages: [0, 0],
        outputCalc: "no",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0.00,
        maxNumberPeople: 0.00,
        minTimeNeeded: 0.00,
        maxTimeNeeded: 0.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.00,
        additionalTimeFactor: "o",
        multiplicationStrand: 1,
        loopStrand: "o", // Do we need all the non used properties on these aux nodes?
        decisionStrand: "o",
        risks: "x",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
	}),
    (loopStart1:LOOPSTART:Activity:AuxiliaryNode {
		// id: 51,
        number: "A0062",
        name: "LoopPointStart01", // Why did we name it 01 and not just 1 like for the other types of aux node?
        // task: 10,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "-",
        minPercentages: [0, 0],
        maxPercentages: [0, 0],
        outputCalc: "loop",
        functionType: "const",
        dependentOn: "loopsPackage",
        weights: [1],
        minUnit: [2], // Why do we have values for the dependentOn here, but not for the other aux types?
        maxUnit: [4],
        minNumberPeople: 0.00,
        maxNumberPeople: 0.00,
        minTimeNeeded: 0.00,
        maxTimeNeeded: 0.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.00,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: 1,
        decisionStrand: "o",
        risks: "x",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
	}),
    (loopEnd1:LOOPEND:Activity:AuxiliaryNode {
		// id: 54,
        number: "A0065",
        name: "LoopPointEnd01",
        // task: 10,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "-",
        minPercentages: [0, 0],
        maxPercentages: [0, 0],
        outputCalc: "-",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0.00,
        maxNumberPeople: 0.00,
        minTimeNeeded: 0.00,
        maxTimeNeeded: 0.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.00,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: 1,
        decisionStrand: "o",
        risks: "x",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
	}),
    (decStart1:DECSTART:Activity:AuxiliaryNode {
        // id: 18,
        number: "A0019",
        name: "DecisionPointStart1",
        // task: 10,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "-",
        minPercentages: [0, 0],
        maxPercentages: [0, 0],
        outputCalc: "decide",
        functionType: "const",
        dependentOn: "brownFieldPlantsPlanned",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0.00,
        maxNumberPeople: 0.00,
        minTimeNeeded: 0.00,
        maxTimeNeeded: 0.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.00,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: 1,
        risks: "x",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
	}),
    (decEnd1:DECEND:Activity:AuxiliaryNode {
		// id: 20,
        number: "A0021",
        name: "DecisionPointEnd1",
        // task: 10,
        // longName: "o",
        description: "o",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "-",
        minPercentages: [0, 0],
        maxPercentages: [0, 0],
        outputCalc: "-",
        functionType: "const",
        dependentOn: "nothing",
        weights: [1],
        minUnit: [0],
        maxUnit: [0],
        minNumberPeople: 0.00,
        maxNumberPeople: 0.00,
        minTimeNeeded: 0.00,
        maxTimeNeeded: 0.00,
        unitTime: "weeks",
        degreeOfParallelization: 0.00,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: 1,
        risks: "x",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: false // Changed to true/false boolean
        // timeChanged: "16/11/2020  12:45:14", // Agree to remove and replace with an automated timestamp?
        // comment: "Renamed following team output review on 13/11/2020. Original data" // Agree to remove?
	})


// Connect up the Activity to its corresponding Task.
CREATE
    (activity3)-[:BELONGS_TO_TASK {
        // task: t.name, // No longer needed as this is the job of the edge?
		// taskLongName: t.longName, // No longer needed as this is the job of the edge?
		// swimlaneId: t.swimlaneId, // No longer needed as this is the job of the edge?
		// swimlaneName: t.swimlaneName, // No longer needed as this is the job of the edge?
		// swimlaneNumber: t.swimlaneNumber, // No longer needed as this is the job of the edge?
		// swimlaneTitle: t.swimlaneTitle, // No longer needed as this is the job of the edge?
		// taskNumber: t.number // No longer needed as this is the job of the edge?
    }]->(task1),
    (activity1)-[:BELONGS_TO_TASK {
        // task: t.name, // No longer needed as this is the job of the edge?
		// taskLongName: t.longName, // No longer needed as this is the job of the edge?
		// swimlaneId: t.swimlaneId, // No longer needed as this is the job of the edge?
		// swimlaneName: t.swimlaneName, // No longer needed as this is the job of the edge?
		// swimlaneNumber: t.swimlaneNumber, // No longer needed as this is the job of the edge?
		// swimlaneTitle: t.swimlaneTitle, // No longer needed as this is the job of the edge?
		// taskNumber: t.number // No longer needed as this is the job of the edge?
    }]->(task2),
    (activity17)-[:BELONGS_TO_TASK]->(task3),
    (activity18)-[:BELONGS_TO_TASK]->(task3),
    (activity19)-[:BELONGS_TO_TASK]->(task4),
    (activity20)-[:BELONGS_TO_TASK]->(task3),
    (activity11)-[:BELONGS_TO_TASK]->(task5),
    (activity21)-[:BELONGS_TO_TASK]->(task5),
    (activity22)-[:BELONGS_TO_TASK]->(task3),
    (activity23)-[:BELONGS_TO_TASK]->(task6),
    (activity24)-[:BELONGS_TO_TASK]->(task3),
    (activity25)-[:BELONGS_TO_TASK]->(task7),
    (activity26)-[:BELONGS_TO_TASK]->(task8),
    (activity4)-[:BELONGS_TO_TASK]->(task9),
    (activity13)-[:BELONGS_TO_TASK]->(task10)


// Create Edges to connect up the Activities.
CREATE
    (StartNode)-[:PRECEDES {
        // id: 1,
        number: "E0001", // Renamed from "edgeNumber".
        // activityFrom: 1, // No longer needed as this is the job of the edge?
        // activityTo: 3, // No longer needed as this is the job of the edge?
        maturity: 0.75,
        predecessorNeedsToFinishBeforeSuccessorFinish: false,
        decision: "o", // Change blank values to not be o to avoid confusion with 0?
        fromLoop: "o", // Change blank values to not be o to avoid confusion with 0?
        toLoopN: "o", // Change blank values to not be o to avoid confusion with 0?
        company: 1, // What should we do with this property? Connect to company?
        active: true, // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data", // Agree to remove?

        // Add the below properties in (from setupactivities.cypher)?
        original: true, 
		used: false
    }]->(activity1),
    (activity1)-[:PRECEDES {
        // id: 2,
        number: "E0002", // Renamed from "edgeNumber".
        // activityFrom: 1, // No longer needed as this is the job of the edge?
        // activityTo: 2, // No longer needed as this is the job of the edge?
        maturity: 0.75,
        predecessorNeedsToFinishBeforeSuccessorFinish: false,
        decision: "o", // Change blank values to not be o to avoid confusion with 0?
        fromLoop: "o", // Change blank values to not be o to avoid confusion with 0?
        toLoopN: "o", // Change blank values to not be o to avoid confusion with 0?
        company: 1, // What should we do with this property? Connect to company?
        active: true, // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data", // Agree to remove?

        // Add the below properties in (from setupactivities.cypher)?
        original: true, 
		used: false
    }]->(activity2),
    (activity2)-[:PRECEDES {
        // id: 3,
        number: "E0004", // Renamed from "edgeNumber".
        // activityFrom: 1, // No longer needed as this is the job of the edge?
        // activityTo: 2, // No longer needed as this is the job of the edge?
        maturity: 0.55,
        predecessorNeedsToFinishBeforeSuccessorFinish: false,
        decision: "o", // Change blank values to not be o to avoid confusion with 0?
        fromLoop: "o", // Change blank values to not be o to avoid confusion with 0?
        toLoopN: "o", // Change blank values to not be o to avoid confusion with 0?
        company: 1, // What should we do with this property? Connect to company?
        active: true, // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data", // Agree to remove?

        // Add the below properties in (from setupactivities.cypher)?
        original: true, 
		used: false
    }]->(EndNode)
// Create some edges to string all activities together.
CREATE
    (activity1)-[:PRECEDES {
        // Properties from "cypher/dbinit/initialsetup/connectactivities.cypher".
        original: true, 
		used: false
    }]->(activity3),
    (activity3)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity4),
    (activity4)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity5),
    (activity5)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity6),
    (activity6)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity7),
    (activity7)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity8),
    (activity8)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity9),
    (activity9)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity10),
    (activity10)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity11),
    (activity11)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity12),
    (activity12)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity13),
    (activity13)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity14),
    (activity14)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity15),
    (activity15)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity16),
    (activity16)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity2),
    (activity5)-[:PRECEDES {
        original: true, 
		used: false
    }]->(multStart1),
    (multStart1)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity6),
    (activity8)-[:PRECEDES {
        original: true, 
		used: false
    }]->(multEnd1),
    (multStart1)-[:PRECEDES { // Is this edge needed?
        original: true, 
		used: false
    }]->(multEnd1),
    (activity2)-[:PRECEDES {
        original: true, 
		used: false
    }]->(loopStart1),
    (loopStart1)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity3),
    (activity3)-[:PRECEDES {
        original: true, 
		used: false
    }]->(loopEnd1),
    (loopEnd1)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity4),
    (loopStart1)-[:PRECEDES { // Is this edge needed?
        original: true, 
		used: false
    }]->(loopEnd1),
    (activity10)-[:PRECEDES {
        original: true, 
		used: false
    }]->(decStart1),
    (decStart1)-[:PRECEDES {
        original: true, 
		used: false,
        decision: false
    }]->(activity11),
    (decStart1)-[:PRECEDES {
        original: true, 
		used: false,
        decision: true
    }]->(activity12),
    (activity11)-[:PRECEDES {
        original: true, 
		used: false
    }]->(decEnd1),
    (activity12)-[:PRECEDES {
        original: true, 
		used: false
    }]->(decEnd1),
    (decStart1)-[:PRECEDES { // Is this edge needed?
        original: true, 
		used: false
    }]->(decEnd1),
    (activity1)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity17),
    (activity17)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity18),
    (activity18)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity19),
    (activity19)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity20),
    (activity20)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity11),
    (activity11)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity21),
    (activity20)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity22),
    (activity10)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity23),
    (activity22)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity23),
    (activity22)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity24),
    (activity12)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity25),
    (activity23)-[:PRECEDES {
        original: true, 
		used: false
    }]->(activity26)


// Create Milestone_Has_Preactivity edges to connect up the Milestones to Activities.
CREATE
    (milestone1)-[:HAS_PREACTIVITY {
        // id: 2,
        number: "MP0005", // Renamed from "milestoneHasPreactivityNumber".
        // milestone: 2, // No longer needed as this is the job of the edge?
        // activity: 86, // No longer needed as this is the job of the edge?
        maturity: 1.00,
        company: 1, // What should we do with this property? Connect to company?
        active: true // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original preactivity.", // Agree to remove?
    }]->(activity1)


// Create Feedback Edges to connect up the Activities to Tasks.
CREATE
    (activity2)-[:PROVIDES_FEEDBACK {
        // id: 216,
        number: "I0247", // Renamed from "feedbackEdgeNumber".
        // fromActivity: 150, // No longer needed as this is the job of the edge?
        // toTask: 26, // No longer needed as this is the job of the edge?
        feedbackInformationDescription: "Final {EP} design",
        company: 1, // What should we do with this property? Connect to company?
        active: true // Changed to true/false boolean
        // timeChanged: "13/10/2021  16:37:00", // Agree to remove and replace with an automated timestamp?
        // comment: "Added following team review on 13/10/2021.", // Agree to remove?
    }]->(task1)


// Create Special Activities.
CREATE (specialactivity1:Activity:MD {
        // id: 8,
        number: "B0010",
        name: "Engineering Launch Support",
        // task: 33,
        // longName: "o",
        description: "Ongoing engineering support for all activities leading up to vehicle launch.",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Autonomous Vehicle Systems",
        minPercentages: [0, 1, 1, 1, 1],
        maxPercentages: [0, 1, 1, 1, 1],
        outputCalc: "no",
        functionType: "linearDiffSlope",
        dependentOn: "numberVariants;productNewness",
        weights: [1],
        minUnit: [1],
        maxUnit: [198],
        minNumberPeople: 5.00,
        maxNumberPeople: 15.00,
        minTimeNeeded: 2.00,
        maxTimeNeeded: 20.00,
        unitTime: "weeks",
        degreeOfParallelization: 1.00,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "You will not have sufficient information regarding potentials or uncertainties in your targeted markets.",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: true // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:20:57", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data" // Agree to remove?
	}),
    (specialactivity2:Activity:MF {
        // id: 8,
        number: "B0019",
        name: "Plant Development",
        // task: 33,
        // longName: "o",
        description: "Ongoing engineering support for all activities leading up to vehicle launch.",
        // evidence: "o",
        // deliverables: "o",
        leadTeam: "-",
        supportTeam: "Body Exteriors;Body Interiors;Chassis;Autonomous Vehicle Systems",
        minPercentages: [0, 1, 1, 1, 1],
        maxPercentages: [0, 1, 1, 1, 1],
        outputCalc: "no",
        functionType: "linearDiffSlope",
        dependentOn: "numberVariants;productNewness",
        weights: [1],
        minUnit: [1],
        maxUnit: [198],
        minNumberPeople: 5.00,
        maxNumberPeople: 15.00,
        minTimeNeeded: 2.00,
        maxTimeNeeded: 20.00,
        unitTime: "weeks",
        degreeOfParallelization: 1.00,
        additionalTimeFactor: "o",
        multiplicationStrand: "o",
        loopStrand: "o",
        decisionStrand: "o",
        risks: "You will not have sufficient information regarding potentials or uncertainties in your targeted markets.",
        // company: 1,
       	active: true, // Changed to true/false boolean
        testing: true // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:20:57", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data" // Agree to remove?
	})


// Create Special Edges to connect up the Special Activities.
CREATE
    (activity6)-[:END_TRIGGERS_START_OF { // Instead of being a PRECEDES - already make it the correct label from "cypher/dbinit/initialsetup/connectactivities.cypher".
        // id: 154,
        number: "F0245", // Renamed from "edgeNumber".
        // activityFrom: 688, // No longer needed as this is the job of the edge?
        // specialActivityFrom: NULL, // No longer needed as this is the job of the edge?
        // activityTo: NULL, // No longer needed as this is the job of the edge?
        // specialActivityTo: 58, // No longer needed as this is the job of the edge?
        // maturity: 1, // Debate if needed at all? Doesn't endsStartsWithEndStart define this?
        // predecessorNeedsToFinishBeforeSuccessorFinish: 0, // Debate if needed at all? Doesn't have any impact does it?
        
        decision: "o", // Change blank values to not be o to avoid confusion with 0?
        fromLoop: "o", // Change blank values to not be o to avoid confusion with 0?
        toLoopN: "o", // Change blank values to not be o to avoid confusion with 0?
        endsStartsWithEndStart: "se",
        company: 1, // What should we do with this property? Connect to company?
        active: true, // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data", // Agree to remove?

        // Properties from "cypher/dbinit/initialsetup/connectactivities.cypher".
        original: true, 
        used: true, 
		cost: 0.0,
		active: true // Why is the property like this when it is a 1 or 0 in the data?
    }]->(specialactivity1),
    (specialactivity1)-[:ENDS_AT_START_OF {
        // id: 99,
        number: "F0176", // Renamed from "edgeNumber".
        // activityFrom: NULL, // No longer needed as this is the job of the edge?
        // specialActivityFrom: 58, // No longer needed as this is the job of the edge?
        // activityTo: 506, // No longer needed as this is the job of the edge?
        // specialActivityTo: NULL, // No longer needed as this is the job of the edge?
        // maturity: 1, // Debate if needed at all? Doesn't endsStartsWithEndStart define this?
        // predecessorNeedsToFinishBeforeSuccessorFinish: 1, // Debate if needed at all? Doesn't have any impact does it?
        
        decision: "o", // Change blank values to not be o to avoid confusion with 0?
        fromLoop: "o", // Change blank values to not be o to avoid confusion with 0?
        toLoopN: "o", // Change blank values to not be o to avoid confusion with 0?
        endsStartsWithEndStart: "es",
        company: 1, // What should we do with this property? Connect to company?
        active: true, // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data", // Agree to remove?

        original: true, 
        used: true, 
		cost: 0.0,
		active: true
    }]->(activity15),
    (activity9)-[:START_TRIGGERS_START_OF {
        // id: 154,
        number: "F0249", // Renamed from "edgeNumber".
        // activityFrom: 688, // No longer needed as this is the job of the edge?
        // specialActivityFrom: NULL, // No longer needed as this is the job of the edge?
        // activityTo: NULL, // No longer needed as this is the job of the edge?
        // specialActivityTo: 58, // No longer needed as this is the job of the edge?
        // maturity: 1, // Debate if needed at all? Doesn't endsStartsWithEndStart define this?
        // predecessorNeedsToFinishBeforeSuccessorFinish: 0, // Debate if needed at all? Doesn't have any impact does it?
        
        decision: "o", // Change blank values to not be o to avoid confusion with 0?
        fromLoop: "o", // Change blank values to not be o to avoid confusion with 0?
        toLoopN: "o", // Change blank values to not be o to avoid confusion with 0?
        endsStartsWithEndStart: "ss",
        company: 1, // What should we do with this property? Connect to company?
        active: true, // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data", // Agree to remove?

        // Properties from "cypher/dbinit/initialsetup/connectactivities.cypher".
        original: true, 
        used: true, 
		cost: 0.0,
		active: true // Why is the property like this when it is a 1 or 0 in the data?
    }]->(specialactivity2),
    (specialactivity2)-[:ENDS_AT_END_OF {
        number: "F0179",
        decision: "o",
        fromLoop: "o",
        toLoopN: "o",
        endsStartsWithEndStart: "ee",
        company: 1,
        active: true,
        original: true, 
        used: true, 
		cost: 0.0,
		active: true
    }]->(activity10)

// Connect up Special Activities to tasks.
CREATE
    (specialactivity1)-[:BELONGS_TO_TASK]->(task9),
    (specialactivity2)-[:BELONGS_TO_TASK]->(task10)


// Create Properties.
// Add a top node for Properties which holds the required fields and data types.
CREATE (PropertyTopNode:Property:TopNode {
        // id: "integer",
        // number: "string",
        name: "string",
        visualOrder: "double",
        displayName: "string",
        displayInformation: "string",
        propertyMinValue: "double",
        propertyMaxValue: "double",
        displayInformationSetToZero: "string",
        displayInformationSetFromZero: "string"
    })
// Create all properties.
CREATE 
    (property1:Property {
        // id: 4,
        // number: "P0004", // Proposed to add a property number or is this a bad idea as can't add to all?
        name: "numberEPVehicles", // Renamed to be name instead of propertyName.
        visualOrder: 6.0,
        displayName: "Number of {EP} Properties",
        displayInformation: "The number of {Engineering Prototype} properties including rigs, bucks and vehicles that will be produced to support the {Engineering Prototype} testing phase.",
        propertyMinValue: 3.0,
        propertyMaxValue: 50.0,
        displayInformationSetToZero: "Removing all {EP} test properties will remove the {EP} build and test phase used by Physical Validation and {Engineering Design} teams.",
        displayInformationSetFromZero: "Adding {EP} test properties will introduce an {EP} build and test phase used by Physical Validation and {Engineering Design} teams."
	}),
    (property2:Property {
        // id: 11,
        // number: "P0011",
        name: "numberMarkets",
        visualOrder: 9.0,
        displayName: "Number of Markets",
        displayInformation: "The number of Markets for selling the vehicle.",
        propertyMinValue: 1.0,
        propertyMaxValue: 198.0,
        displayInformationSetToZero: "Removing all markets will TBC.",
        displayInformationSetFromZero: "Adding more markets will TBC."
	}),
    (property3:Property {
        // id: 19,
        // number: "P0019",
        name: "numberGreenFieldPlants",
        visualOrder: 19.0,
        displayName: "Number of Green Field Plants",
        displayInformation: "The number of Green field plants.",
        propertyMinValue: 0,
        propertyMaxValue: 4,
        displayInformationSetToZero: "Removing all green field plants will TBC.",
        displayInformationSetFromZero: "Adding more green field plants will TBC."
	}),
    (property4:Property {
        // id: 26,
        // number: "P0026",
        name: "numberPlants",
        visualOrder: 26.0,
        displayName: "Number of Plants",
        displayInformation: "The number of plants.",
        propertyMinValue: 1,
        propertyMaxValue: 8,
        displayInformationSetToZero: "Removing all plants will TBC.",
        displayInformationSetFromZero: "Adding more plants will TBC."
	}),
    (property5:Property {
        // id: 1,
        // number: "P0001",
        name: "nothing",
        visualOrder: 100.0,
        displayName: "Nothing",
        displayInformation: "The nothing property.",
        propertyMinValue: NULL,
        propertyMaxValue: NULL,
        displayInformationSetToZero: "n/a",
        displayInformationSetFromZero: "n/a"
	}),
    (property6:Property {
        // id: 4,
        // number: "P0004", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "edginess", // Rename to be name?
        //visualOrder: 6.0,
        //displayName: "Number of {EP} Properties",
        //displayInformation: "The number of {Engineering Prototype} properties including rigs, bucks and vehicles that will be produced to support the {Engineering Prototype} testing phase.",
        propertyMinValue: 1.0,
        propertyMaxValue: 5.0
	}),
    (property7:Property {
        // id: 19,
        // number: "P0019", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "numberClayOrVirtualModels", // Rename to be name?
        //visualOrder: 19.0,
        //displayName: "Number of Green Field Plants",
        //displayInformation: "The number of Green field plants.",
        propertyMinValue: 1,
        propertyMaxValue: 3
	}),
    (property8:Property {
        // id: 19,
        // number: "P0019", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "companyCategory", // Rename to be name?
        //visualOrder: 19.0,
        //displayName: "Number of Green Field Plants",
        //displayInformation: "The number of Green field plants.",
        propertyMinValue: 1.0,
        propertyMaxValue: 3.0
	}),
    (property9:Property {
        // id: 19,
        // number: "P0019", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "companyStructuredness", // Rename to be name?
        //visualOrder: 19.0,
        //displayName: "Number of Green Field Plants",
        //displayInformation: "The number of Green field plants.",
        propertyMinValue: 1,
        propertyMaxValue: 3
	}),
    (property10:Property {
        // id: 19,
        // number: "P0019", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "technologyLevel", // Rename to be name?
        //visualOrder: 19.0,
        //displayName: "Number of Green Field Plants",
        //displayInformation: "The number of Green field plants.",
        propertyMinValue: 1,
        propertyMaxValue: 3
	}),
    (property11:Property {
        // id: 19,
        // number: "P0019", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "newOEM", // Rename to be name?
        //visualOrder: 19.0,
        //displayName: "Number of Green Field Plants",
        //displayInformation: "The number of Green field plants.",
        propertyMinValue: false,
        propertyMaxValue: true
	}),
    (property12:Property {
        // id: 11,
        // number: "P0011", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "numberMarkets", // Rename to be name?
        visualOrder: 9.0,
        displayName: "Number of Markets",
        displayInformation: "The number of Markets for selling the vehicle.",
        propertyMinValue: 1.0,
        propertyMaxValue: 198.0,
        displayInformationSetToZero: "Removing all markets will TBC.",
        displayInformationSetFromZero: "Adding more markets will TBC."
	}),
    (property13:Property {
        // id: 26,
        // number: "P0026", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "numberRegions", // Rename to be name?
        propertyMinValue: 1.0,
        propertyMaxValue: 14.0
	}),
    (property14:Property {
        // id: 26,
        // number: "P0026", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "weightedRegions", // Rename to be name?
        propertyMinValue: 1.0,
        propertyMaxValue: 16.5
	}),
    (property15:Property {
        // id: 26,
        // number: "P0026", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "yearsVehicleInMarket", // Rename to be name?
        propertyMinValue: 7.0,
        propertyMaxValue: 7.0
	}),
    (property16:Property {
        // id: 11,
        // number: "P0011", // Proposed to add a property number or is this a bad idea as can't add to all?
        propertyName: "StylingOEMSizeFactor", // Rename to be name?
        propertyMinValue: 1.6,
        propertyMaxValue: toFloat(1.6*sqrt(3))
	})
// Create an edge to connect up to the master nodes.
CREATE
    (PropertyTopNode)-[:INPUT]->(graphTopNode2),
    (property1)-[:INPUT]->(PropertyTopNode),
    (property2)-[:INPUT]->(PropertyTopNode),
    (property3)-[:INPUT]->(PropertyTopNode),
    (property4)-[:INPUT]->(PropertyTopNode),
    (property5)-[:INPUT]->(PropertyTopNode),
    (property6)-[:INPUT]->(PropertyTopNode),
    (property7)-[:INPUT]->(PropertyTopNode),
    (property8)-[:INPUT]->(PropertyTopNode),
    (property9)-[:INPUT]->(PropertyTopNode),
    (property10)-[:INPUT]->(PropertyTopNode),
    (property11)-[:INPUT]->(PropertyTopNode),
    (property12)-[:INPUT]->(PropertyTopNode),
    (property13)-[:INPUT]->(PropertyTopNode),
    (property14)-[:INPUT]->(PropertyTopNode),
    (property15)-[:INPUT]->(PropertyTopNode),
    (property16)-[:INPUT]->(PropertyTopNode)
// Create a dependant edge to connect up dependant property nodes.
CREATE
    (property4)-[:DEPENDANT_ON]->(property3),
    (property16)-[:DEPENDENT_ON]->(property8)
// Create an output edge to connect the Top Property Node to the master node.
CREATE
    (graphTopNode3)-[:OUTPUT]->(PropertyTopNode)


// Connect up the Property to its linked Activities.
CREATE
    (activity1)-[:HAS_PROPERTY { // Previously was "IS_INPUT_FOR" I think? Proposal to change to be clearer.
        id: 3 // Delete long term but left in to mkae cypher script work.
        // property: 3, // No longer needed as this is the job of the edge?
        // activity: 13, // No longer needed as this is the job of the edge?
    }]->(property1),
    (activity10)-[:HAS_PROPERTY { // Previously was "IS_INPUT_FOR" I think? Proposal to change to be clearer.
        id: 4 // Delete long term but left in to mkae cypher script work.
        // property: 2, // No longer needed as this is the job of the edge?
        // activity: 16, // No longer needed as this is the job of the edge?
    }]->(property2),
    (activity13)-[:HAS_PROPERTY { // Previously was "IS_INPUT_FOR" I think? Proposal to change to be clearer.
        id: 5 // Delete long term but left in to mkae cypher script work.
        // property: 8, // No longer needed as this is the job of the edge?
        // activity: 19, // No longer needed as this is the job of the edge?
    }]->(property4),
    (activity4)-[:HAS_PROPERTY { // Previously was "IS_INPUT_FOR" I think? Proposal to change to be clearer.
        id: 29 // Delete long term but left in to mkae cypher script work.
        // property: 8, // No longer needed as this is the job of the edge?
        // activity: 19, // No longer needed as this is the job of the edge?
    }]->(property1)


// Connect up the Property as an Output_Calc of its correct Activities.
CREATE
    (activity8)-[:OUTPUT_CALC {
        id: 4 // Delete long term but left in to make cypher script work.
        // property: 3, // No longer needed as this is the job of the edge?
        // activity: 13, // No longer needed as this is the job of the edge?
    }]->(property2)


// Create Deliverables.
CREATE (deliverable1:Deliverable {
        // id: 5,
        number: "D0005",
        description: "Set of quantified targets against which second stage benchmarking results are measured",
        displayAtMilestone: false // Changed to true/false boolean
    }),
    (deliverable2:Deliverable {
        // id: 34,
        number: "D0034",
        description: "Initial costed business case based on concept {BoM} cost and margin with system level target costs identified",
        displayAtMilestone: true // Changed to true/false boolean
    })


// Create Task_Has_Deliverable relationship.
CREATE
    (task1)-[:HAS_DELIVERABLE {
        // id: 896,
        number: "TD1205", // Renamed from "taskHasDeliverableNumber".
        // deliverable: 1304, // No longer needed as this is the job of the edge?
        // task: 347, // No longer needed as this is the job of the edge?
        company: 1,
        active: true // Changed to true/false boolean. Debate changing for for task has deliverable. may not want to display all deliverables at task level, see THD table in excel. Maybe store at deliverable level?
        // timeChanged: "13/10/2021  16:37:00", // Agree to remove and replace with an automated timestamp?
        // comment: "Added following team review on 13/10/2021.", // Agree to remove?
    }]->(deliverable1)


// Create Activity_Has_Deliverable relationship.
CREATE
    (activity1)-[:HAS_DELIVERABLE {
        // id: 991,
        number: "AD1330", // Renamed from "activityDeliverable".
        // deliverable: 1304, // No longer needed as this is the job of the edge?
        // activity: 347, // No longer needed as this is the job of the edge?
        // specialactivity: NULL, // No longer needed as this is the job of the edge?
        loopN: 2,
        company: 0,
        active: false // Changed to true/false boolean
        // timeChanged: "13/10/2021  16:37:00", // Agree to remove and replace with an automated timestamp?
        // comment: "Added following team review on 13/10/2021.", // Agree to remove?
    }]->(deliverable1),
    (specialactivity1)-[:HAS_DELIVERABLE {
        // id: 1008,
        number: "AD1350", // Renamed from "activityDeliverable".
        // deliverable: 1324, // No longer needed as this is the job of the edge?
        // activity: 224, // No longer needed as this is the job of the edge?
        // specialactivity: NULL, // No longer needed as this is the job of the edge?
        loopN: 0,
        company: 0,
        active: false // Changed to true/false boolean
        // timeChanged: "13/10/2021  16:37:00", // Agree to remove and replace with an automated timestamp?
        // comment: "Added following team review on 13/10/2021.", // Agree to remove?
    }]->(deliverable2)


// Add Tag.
CREATE (tag9:Tag {
        // id: 9,
        number: "U0024",
		name: "Review",
		description: "All activities and deliverables related to conducting the team reviews displayed on the timing plan.",
        visualOrder: 9
    })
// Create an edge to connect up to the master node.
CREATE
    (tag9)-[:INPUT]->(graphTopNode2)


// Create Node_Has_Tag relationship.
CREATE
    (activity1)-[:HAS_TAG {
        // id: 400,
        number: "N0400"
        // activity: 2, // No longer needed as this is the job of the edge?
        // specialactivity: NULL, // No longer needed as this is the job of the edge?
        // milestone: NULL, // No longer needed as this is the job of the edge?
        // deliverable: NULL, // No longer needed as this is the job of the edge?
        // tag: 6 // No longer needed as this is the job of the edge?
    }]->(tag9),
    (specialactivity1)-[:HAS_TAG {
        // id: 370,
        number: "N0370"
        // activity: NULL, // No longer needed as this is the job of the edge?
        // specialactivity: 71, // No longer needed as this is the job of the edge?
        // milestone: NULL, // No longer needed as this is the job of the edge?
        // deliverable: NULL, // No longer needed as this is the job of the edge?
        // tag: 5 // No longer needed as this is the job of the edge?
    }]->(tag9),
    (milestone1)-[:HAS_TAG {
        // id: 411,
        number: "N0411"
        // activity: NULL, // No longer needed as this is the job of the edge?
        // specialactivity: NULL, // No longer needed as this is the job of the edge?
        // milestone: 9, // No longer needed as this is the job of the edge?
        // deliverable: NULL, // No longer needed as this is the job of the edge?
        // tag: 2 // No longer needed as this is the job of the edge?
    }]->(tag9),
    (deliverable1)-[:HAS_TAG {
        // id: 231,
        number: "N0231"
        // activity: NULL, // No longer needed as this is the job of the edge?
        // specialactivity: NULL, // No longer needed as this is the job of the edge?
        // milestone: NULL, // No longer needed as this is the job of the edge?
        // deliverable: 914, // No longer needed as this is the job of the edge?
        // tag: 0 // No longer needed as this is the job of the edge?
    }]->(tag9)


// Create Waypoints.
CREATE (waypoint2:Waypoint {
        // id: 2,
        number: "W0002",
        name: "Check {PA} Vehicle {BoM}",
        // longName: "Check {Project Approval} {Bill of Materials}", // Delete long term.
        description: "TBC",
        // company: 1,
       	active: true // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:20:57", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data" // Agree to remove?
        // swimlane: 1, // Add swimlane as we want waypoints connected to swimlanes?
	})
// Connect up the Waypoint to its corresponding Swimlane.
CREATE
    (waypoint2)-[:BELONGS_TO_SWIMLANE {
        // swimlaneNumber: 3, // No longer needed as this is the job of the edge?
        // swimlaneTitle: "Styling", // No longer needed.
        needed: true
    }]->(swimlane3)
// Connect up the Waypoint to its corresponding Activity.
CREATE
    (activity1)-[:PRECEDES {
        // id: 2,
        number: "X0002",
        // waypoint: 2, // No longer needed as this is the job of the edge?
        // activity: 628, // No longer needed as this is the job of the edge?
        // specialactivity: NULL, // No longer needed as this is the job of the edge?
        maturity: 1.0,
        company: 1, // What should we do with this property? Connect to company?
        active: true // Changed to true/false boolean
        // timeChanged: "12/02/2021  15:22:31", // Agree to remove and replace with an automated timestamp?
        // comment: "Original data", // Agree to remove?
    }]->(waypoint2)


// Create the time node.
CREATE (t:Time {
        dayCount: 1.0
    })
// Create an edge to connect up to the master node.
CREATE
    (t)-[:INPUT]->(graphTopNode2)


// Added on 15/03/2022 after Logging Discussion.

// Add some TopNodes for Event types.
CREATE
    (StartDelayEventTopNode:EventTopNode:TopNode:StartDelay {
        // Note: Success is calculated as to if the valueNew matches the valueNewActual.
        // Note: Increase is calculated if valueNew > valueOld. If not, decrease.
        impact: "The activity start was delayed by {valueNew} {valueUnit}.", // The successful impact of the change. Shown in the "User Actions" table as "Impact". {} are replaced by variables.
        number: "TEV0001", // The top node event number, a unique value to identify each event. Discuss if needed?
        type: "startDelay", // The type of event. Shown in the "User Actions" table as "Action Name".
        typeName: "Start Delay" // From old events. Discuss if needed. Displayed in the UI.
    }),
    (DurationChangedWithoutWorkloadChangeEventTopNode:EventTopNode:TopNode:DurationChangedWithoutWorkloadChange {
        // Note: Success is calculated as to if the valueNew matches the valueNewActual.
        // Note: Increase is calculated if valueNew > valueOld. If not, decrease.
        impactSuccessIncrease: "Activity duration was changed to {valueNew} {valueUnit} without changing the total workload. Resource needs adapted accordingly.",
        impactFailureIncrease: "Activity duration set to the maximum allowable value of {valueNew} {valueUnit}. Activity duration was changed without changing the total workload. Resource needs adapted accordingly.",
        impactSuccessDecrease: "Activity duration was changed to {valueNew} {valueUnit} without changing the total workload. Resource needs adapted accordingly.",
        impactFailureDecrease: "Activity duration set to the minimum possible value of {activityMinimumTimeNeeded} {valueUnit}. It cannot be any shorter as it is held up. Activity duration was changed without changing the total workload. Resource needs adapted accordingly.",
        number: "TEV0002",
        type: "durationChangedWithoutWorkloadChange",
        typeName: "Duration Change (Without Workload Change)"
    }),
    (DurationChangedWithWorkloadChangeEventTopNode:EventTopNode:TopNode:DurationChangedWithWorkloadChange {
        impactSuccessIncrease: "Activity duration was changed to {valueNew} {valueUnit} with workload change. Workload adapted accordingly without changing team sizes.",
        impactFailureIncrease: "Activity duration set to the maximum allowable value of {valueNew} {valueUnit}. Activity duration was changed with workload change. Workload adapted accordingly without changing team sizes.",
        impactSuccessDecrease: "Activity duration was changed to {valueNew} {valueUnit} with workload change. Workload adapted accordingly without changing team sizes.",
        impactFailureDecrease: "Activity duration set to the minimum possible value of {valueNew} {valueUnit}. It cannot be any shorter as it is held up. Activity duration was changed with workload change. Workload adapted accordingly without changing team sizes.",
        number: "TEV0003",
        type: "durationChangedWithWorkloadChange",
        typeName: "Duration Change (With Workload Change)"
    }),
    (ResourcesChangedWithoutWorkloadChangeEventTopNode:EventTopNode:TopNode:ResourcesChangedWithoutWorkloadChange {
        impactSuccess: "Activity resources changed without changing the total workload. Duration adapted accordingly from {durationValueOld} {durationValueUnit} to {durationValueNew} {durationValueUnit}.",
        number: "TEV0004",
        type: "resourcesChangedWithoutWorkloadChange",
        typeName: "Resources Change (Without Workload Change)"
    }),
    (ResourcesChangedWithWorkloadChangeEventTopNode:EventTopNode:TopNode:ResourcesChangedWithWorkloadChange {
        impactSuccess: "Activity resources changed with workload change. Duration not changed.",
        number: "TEV0005",
        type: "resourcesChangedWithWorkloadChange",
        typeName: "Resources Change (With Workload Change)"
    }),
    (MaturityChangeEventTopNode:EventTopNode:TopNode:MaturityChange {
        impactSuccessIncrease: "The activity's maturity was set to {valueNew}%.",
        impactFailureIncrease: "The activity's maturity remains at {valueOriginal}% as it is being held up by incomplete predecessors.",
        impactSuccessDecrease: "The activity's maturity was set to {valueNew}%.",
        number: "TEV0006",
        type: "maturityChange",
        typeName: "Change Activity Maturity"
    }),
    (DeleteActivityEventTopNode:EventTopNode:TopNode:DeleteActivity {
        impactSuccess: "Activity was deleted.",
        number: "TEV0007",
        type: "deleteActivity",
        typeName: "Delete Activity"
    }),
    (DeleteMilestoneEventTopNode:EventTopNode:TopNode:DeleteMilestone {
        impactSuccess: "Milestone was deleted.",
        impactFailure: "Milestone was not deleted as need a minimum of two milestones on the plan.",
        number: "TEV0008",
        type: "deleteMilestone",
        typeName: "Delete Milestone"
    }),
    (MarkAlreadyExecutedEventTopNode:EventTopNode:TopNode:MarkAlreadyExecuted {
        impactSuccess: "Activity marked as completed.",
        impactFailure: "Activity was not marked as completed as it has incomplete predecessors.",
        number: "TEV0009",
        type: "markAlreadyExecuted",
        typeName: "Mark Already Executed"
    }),
    (ChangePropertyValueEventTopNode:EventTopNode:TopNode:ChangePropertyValue {
        impactSuccess: "Property value changed from {valueOriginal} to {valueNew}. Dependent activities scaled based on new properties.",
        number: "TEV0010",
        type: "changePropertyValue",
        typeName: "Change Property Value"
    }),
    (DeleteSwimlaneEventTopNode:EventTopNode:TopNode:DeleteSwimlane {
        impactSuccess: "Swimlane deleted. All activities in swimlane subsequently deleted.",
        number: "TEV0011",
        type: "deleteSwimlane",
        typeName: "Delete Swimlane"
    }),
    (HideSwimlaneEventTopNode:EventTopNode:TopNode:HideSwimlane {
        impactSuccess: "Swimlane hidden. All activities in swimlane hidden.",
        number: "TEV0012",
        type: "hideSwimlane",
        typeName: "Hide Swimlane"
    }),
    (DeleteTagEventTopNode:EventTopNode:TopNode:DeleteTag {
        impactSuccess: "Tag deleted. All activities with tag deleted.",
        number: "TEV0013",
        type: "deleteTag",
        typeName: "Delete Tag"
    }),
    (HideTagEventTopNode:EventTopNode:TopNode:HideTag {
        impactSuccess: "Tag hidden. All activities with tag hidden.",
        number: "TEV0014",
        type: "hideTag",
        typeName: "Hide Tag"
    }),
    (SpecialActivityDisappearedSystemTopNode:SystemTopNode:TopNode:System:SpecialActivityDisappeared {
        impactSuccess: "Special activity disappeared as its duration became negative due to changes to its related activities.",
        number: "TSYS001",
        type: "specialActivityDisappeared",
        typeName: "Special Activity Disappeared"
    }),
    (DeleteSwimlaneSystemTopNode:SystemTopNode:TopNode:System:DeleteSwimlane {
        impactSuccess: "Swimlane deleted as all activities in swimlane have been deleted.",
        number: "TSYS002",
        type: "deleteSwimlane",
        typeName: "Delete Swimlane"
    }),
    (DeleteTaskSystemTopNode:SystemTopNode:TopNode:System:DeleteTask {
        impactSuccess: "Task deleted as all activities in task have been deleted.",
        number: "TSYS003",
        type: "deleteTask",
        typeName: "Delete Task"
    }),
    (DeleteMilestoneSystemTopNode:SystemTopNode:TopNode:System:DeleteMilestone {
        impactSuccess: "Milestone deleted as all linked activities to the milestone have been deleted.",
        number: "TSYS004",
        type: "deleteMilestone",
        typeName: "Delete Milestone"
    }),
    (RecommendationSystemTopNode:SystemTopNode:TopNode:System:Recommendation {
        message: "Unique message per recommendation.",
        number: "REC001",
        type: "recommendation",
        typeName: "recommendation"
    })

// Create edges to connect up the top nodes to the Meta graph top node.
CREATE
    (SystemEventTopNode)-[:INPUT]->(graphTopNode1),
    (StartDelayEventTopNode)-[:INPUT]->(graphTopNode1),
    (DurationChangedWithoutWorkloadChangeEventTopNode)-[:INPUT]->(graphTopNode1),
    (DurationChangedWithWorkloadChangeEventTopNode)-[:INPUT]->(graphTopNode1),
    (ResourcesChangedWithoutWorkloadChangeEventTopNode)-[:INPUT]->(graphTopNode1),
    (ResourcesChangedWithWorkloadChangeEventTopNode)-[:INPUT]->(graphTopNode1),
    (MaturityChangeEventTopNode)-[:INPUT]->(graphTopNode1),
    (DeleteActivityEventTopNode)-[:INPUT]->(graphTopNode1),
    (DeleteMilestoneEventTopNode)-[:INPUT]->(graphTopNode1),
    (MarkAlreadyExecutedEventTopNode)-[:INPUT]->(graphTopNode1),
    (ChangePropertyValueEventTopNode)-[:INPUT]->(graphTopNode1),
    (DeleteSwimlaneEventTopNode)-[:INPUT]->(graphTopNode1),
    (HideSwimlaneEventTopNode)-[:INPUT]->(graphTopNode1),
    (DeleteTagEventTopNode)-[:INPUT]->(graphTopNode1),
    (HideTagEventTopNode)-[:INPUT]->(graphTopNode1),
    (SpecialActivityDisappearedSystemTopNode)-[:INPUT]->(graphTopNode1),
    (DeleteSwimlaneSystemTopNode)-[:INPUT]->(graphTopNode1),
    (DeleteTaskSystemTopNode)-[:INPUT]->(graphTopNode1),
    (DeleteMilestoneSystemTopNode)-[:INPUT]->(graphTopNode1),
    (RecommendationSystemTopNode)-[:INPUT]->(graphTopNode1)


// Added on 09/03/2022 after Master Graph Review.
// Updated on 22/03/2022 following new direction on Events nodes.

// Create some event nodes. // The properties here are from a combination of the old event node properties and the proposed additional properties from ticket https://git.rle.de/sdp/sdpweb/-/issues/731.
CREATE 
    (event1:Event:UserAction:StartDelay:Success {
        // affectedEntityName: "Styling Activity", // The name of the changed node. Shown in the "User Actions" table as "Affected Entity Name". Agreed not needed on Event node.
        // affectedEntityNumber: "A0003", // The number of the changed node. Shown in the "User Actions" table as "Affected Entity Number". Agreed not needed on Event node.
        // impact: "The activity start was delayed by 20.0 days.", // The impact of the change. Shown in the "User Actions" table as "Impact". Moved to event TopNode.
        // changeDirection: "Increase", // Consider if we need this for calling the correct impact string from the TopNode? Agreed not to need on 16/03/2022.
        impact: "The activity start was delayed by 20.0 days.",
        // impactSuccess: true, // Consider if we need this for calling the correct impact string from the TopNode? Agreed not to need on 16/03/2022.
        mostRecent: false, // From old events. Discuss if we need.
        number: "EV0001", // The event number, a unique value to identify each event. Discuss if needed?
        // taskName: "Colour and Material Theme Development", // The name of the changed node's task. Shown in the "User Actions" table as "Task Name". Save Task number instead of name? Agreed not needed on Event node.
        timestamp: "2021-12-13T14:02:38.465000000", // The timestamp of when the change was made. Shown in the "User Actions" table as "Timestamp".
        // type: "startDelay", // The type of event. Shown in the "User Actions" table as "Action Name". Moved to event TopNode.
        // typeName: "startDelay", // From old events. Discuss if needed. Moved to event TopNode.
        user: "lbangs", // The user who made the change.
        valueOriginal: 0.0, // The original value before the change. Shown in the "User Actions" table as "Original Value".
        valueNew: 20.0, // The requested new value by the user. Shown in the "User Actions" table as "New Value".
        valueNewActual: 20.0, // The actual new value after the change. Used in the Impact column string in the "User Actions" table.
        valueUnit: "days" // The unit of the original and new value. Shown in the "User Actions" table as "Value Unit".
    }),
    (event2:Event:UserAction:DurationChangedWithoutWorkloadChange:Success:Increase {
        impact: "Activity duration was changed to 30 days without changing the total workload. Resource needs adapted accordingly.",
        mostRecent: false,
        number: "EV0002",
        timestamp: "2021-12-13T14:02:38.465000000",
        user: "ekohlwey",
        valueOriginal: 16.0,
        valueNew: 30.0,
        valueNewActual: 30.0,
        valueUnit: "days"
    }),
    (event3:Event:UserAction:DurationChangedWithoutWorkloadChange:Failure:Increase {
        impact: "Activity duration set to the maximum allowable value of 20 days. Activity duration was changed without changing the total workload. Resource needs adapted accordingly.",
        mostRecent: false,
        number: "EV0003",
        timestamp: "2021-12-13T14:02:38.465000000",
        user: "ukloss",
        valueOriginal: 16.0,
        valueNew: 30.0,
        valueNewActual: 20.0,
        valueUnit: "days"
    }),
    (event4:Event:UserAction:DurationChangedWithoutWorkloadChange:Success:Decrease {
        impact: "Activity duration was changed to 3 weeks without changing the total workload. Resource needs adapted accordingly.",
        mostRecent: false,
        number: "EV0004",
        timestamp: "2021-12-13T14:02:38.465000000",
        user: "ukloss",
        valueOriginal: 4.0,
        valueNew: 3.0,
        valueNewActual: 3.0,
        valueUnit: "weeks"
    }),
    (event5:Event:UserAction:DurationChangedWithoutWorkloadChange:Failure:Decrease {
        impact: "Activity duration set to the minimum possible value of 3 weeks. It cannot be any shorter as it is held up. Activity duration was changed without changing the total workload. Resource needs adapted accordingly.",
        mostRecent: false,
        number: "EV0005",
        timestamp: "2021-12-13T14:44:00.00000000",
        user: "lbangs",
        valueOriginal: 4.0,
        valueNew: 2.0,
        valueNewActual: 3.0,
        valueUnit: "weeks"
    }),
    (event6:Event:UserAction:DurationChangedWithWorkloadChange:Success:Increase {
        impact: "Activity duration was changed to 3 months with workload change. Workload adapted accordingly without changing team sizes.",
        mostRecent: false,
        number: "EV0006",
        timestamp: "2021-12-13T14:44:00.00000000",
        user: "dwilloughby",
        valueOriginal: 2.0,
        valueNew: 3.0,
        valueNewActual: 3.0,
        valueUnit: "months"
    }),
    (event7:Event:UserAction:DurationChangedWithWorkloadChange:Failure:Increase {
        impact: "Activity duration set to the maximum allowable value of 3 months. Activity duration was changed with workload change. Workload adapted accordingly without changing team sizes.",
        mostRecent: false,
        number: "EV0007",
        timestamp: "2021-12-13T14:44:00.00000000",
        user: "ekohlwey",
        valueOriginal: 2.0,
        valueNew: 5.0,
        valueNewActual: 3.0,
        valueUnit: "months"
    }),
    (event8:Event:UserAction:DurationChangedWithWorkloadChange:Success:Decrease {
        impact: "Activity duration was changed to 5 days with workload change. Workload adapted accordingly without changing team sizes.",
        mostRecent: false,
        number: "EV0008",
        timestamp: "2021-12-13T14:44:00.00000000",
        user: "lbangs",
        valueOriginal: 10.0,
        valueNew: 5.0,
        valueNewActual: 5.0,
        valueUnit: "days"
    }),
    (event9:Event:UserAction:DurationChangedWithWorkloadChange:Failure:Decrease {
        impact: "Activity duration set to the minimum possible value of 7 days. It cannot be any shorter as it is held up. Activity duration was changed with workload change. Workload adapted accordingly without changing team sizes.",
        mostRecent: false,
        number: "EV0009",
        timestamp: "2021-12-13T14:44:00.00000000",
        user: "ekohlwey",
        valueOriginal: 10.0,
        valueNew: 5.0,
        valueNewActual: 7.0,
        valueUnit: "days"
    }),
    (event10:Event:UserAction:ResourcesChangedWithoutWorkloadChange:Success {
        impact: "Activity resources changed without changing the total workload. Duration adapted accordingly from 44 days to 44 days.",
        mostRecent: false,
        number: "EV0010",
        timestamp: "2021-12-07T06:39:00.00000000",
        user: "ukloss",
        valueOriginal: 9.0,
        valueNew: 6.0,
        valueNewActual: 6.0,
        valueUnit: "total resource"
    }),
    (event11:Event:UserAction:ResourcesChangedWithWorkloadChange:Success {
        impact: "Activity resources changed with workload change. Duration not changed.",
        mostRecent: false,
        number: "EV0011",
        timestamp: "2021-12-07T06:39:00.00000000",
        user: "lbangs",
        valueOriginal: 9.0,
        valueNew: 4.0,
        valueNewActual: 4.0,
        valueUnit: "total resource"
    }),
    (event12:Event:UserAction:MaturityChange:Success:Increase {
        impact: "The activity's maturity was set to 50%.",
        mostRecent: false,
        number: "EV0012",
        timestamp: "2021-12-02T04:59:00.00000000",
        user: "ekohlwey",
        valueOriginal: 0.0,
        valueNew: 50.0,
        valueNewActual: 50.0,
        valueUnit: "%"
    }),
    (event13:Event:UserAction:MaturityChange:Failure:Increase {
        impact: "The activity's maturity remains at 0% as it is being held up by incomplete predecessors.",
        mostRecent: false,
        number: "EV0013",
        timestamp: "2021-11-30T22:34:00.00000000",
        user: "lbangs",
        valueOriginal: 0.0,
        valueNew: 50.0,
        valueNewActual: 0.0,
        valueUnit: "%"
    }),
    (event14:Event:UserAction:MaturityChange:Success:Decrease {
        impact: "The activity's maturity was set to 30%.",
        mostRecent: false,
        number: "EV0014",
        timestamp: "2021-11-29T16:09:00.00000000",
        user: "ekohlwey",
        valueOriginal: 80.0,
        valueNew: 30.0,
        valueNewActual: 30.0,
        valueUnit: "%"
    }),
    (event15:Event:UserAction:DeleteActivity:Success {
        impact: "Activity was deleted.",
        mostRecent: false,
        number: "EV0015",
        timestamp: "2021-11-27T03:19:00.00000000",
        user: "ukloss",
        valueOriginal: 40.0,
        valueNew: 0.0,
        valueNewActual: 0.0,
        valueUnit: "days"
    }),
    (event16:Event:UserAction:DeleteMilestone:Success {
        impact: "Milestone was deleted.",
        mostRecent: false,
        number: "EV0016",
        timestamp: "2021-11-25T20:54:00.00000000",
        user: "lbangs",
        valueOriginal: "n/a",
        valueNew: "n/a",
        valueNewActual: "n/a",
        valueUnit: "n/a"
    }),
    (event17:Event:UserAction:DeleteMilestone:Failure {
        impact: "Milestone was not deleted as need a minimum of two milestones on the plan.",
        mostRecent: false,
        number: "EV0017",
        timestamp: "2021-11-24T14:29:00.00000000",
        user: "dwilloughby",
        valueOriginal: "n/a",
        valueNew: "n/a",
        valueNewActual: "n/a",
        valueUnit: "n/a"
    }),
    (event18:Event:UserAction:MarkAlreadyExecuted:Success {
        impact: "Activity marked as completed.",
        mostRecent: false,
        number: "EV0018",
        timestamp: "2021-11-23T08:04:00.00000000",
        user: "ekohlwey",
        valueOriginal: 24.0,
        valueNew: 0.0,
        valueNewActual: 0.0,
        valueUnit: "days"
    }),
    (event19:Event:UserAction:MarkAlreadyExecuted:Failure {
        impact: "Activity was not marked as completed as it has incomplete predecessors.",
        mostRecent: false,
        number: "EV0019",
        timestamp: "2021-11-22T01:39:00.00000000",
        user: "lbangs",
        valueOriginal: 24.0,
        valueNew: 0.0,
        valueNewActual: 24.0,
        valueUnit: "days"
    }),
    (event20:Event:UserAction:ChangePropertyValue:Success {
        impact: "Property value changed from 2 to 3. Dependent activities scaled based on new properties.",
        mostRecent: false,
        number: "EV0020",
        timestamp: "2021-11-23T01:39:00.00000000",
        user: "ekohlwey",
        valueOriginal: 2.0,
        valueNew: 3.0,
        valueNewActual: 3.0,
        valueUnit: "n/a"
    }),
    (event21:Event:UserAction:DeleteSwimlane:Success {
        impact: "Swimlane deleted. All activities in swimlane subsequently deleted.",
        mostRecent: false,
        number: "EV0021",
        timestamp: "2021-11-24T01:39:00.00000000",
        user: "ukloss",
        valueOriginal: "n/a",
        valueNew: "n/a",
        valueNewActual: "n/a",
        valueUnit: "n/a"
    }),
    (event22:Event:UserAction:HideSwimlane:Success {
        impact: "Swimlane hidden. All activities in swimlane hidden.",
        mostRecent: false,
        number: "EV0022",
        timestamp: "2021-11-25T01:39:00.00000000",
        user: "ukloss",
        valueOriginal: "n/a",
        valueNew: "n/a",
        valueNewActual: "n/a",
        valueUnit: "n/a"
    }),
    (event23:Event:UserAction:DeleteTag:Success {
        impact: "Tag deleted. All activities with tag deleted.",
        mostRecent: false,
        number: "EV0023",
        timestamp: "2021-11-26T01:39:00.00000000",
        user: "lbangs",
        valueOriginal: "n/a",
        valueNew: "n/a",
        valueNewActual: "n/a",
        valueUnit: "n/a"
    }),
    (event24:Event:UserAction:HideTag:Success {
        impact: "Tag hidden. All activities with tag hidden.",
        mostRecent: false,
        number: "EV0024",
        timestamp: "2021-11-27T01:39:00.00000000",
        user: "dwilloughby",
        valueOriginal: "n/a",
        valueNew: "n/a",
        valueNewActual: "n/a",
        valueUnit: "n/a"
    }),
    (event25:Event:System:SpecialActivityDisappeared {
        impact: "Special activity disappeared as its duration became negative due to changes to its related activities.",
        mostRecent: false,
        number: "EV0025",
        timestamp: "2021-11-25T01:39:00.00000000"
    }),
    (event26:Event:System:DeleteSwimlane {
        impact: "Swimlane deleted as all activities in swimlane have been deleted.",
        mostRecent: true,
        number: "EV0026",
        timestamp: "2021-11-25T01:39:00.00000000"
    }),
    (event27:Event:System:DeleteTask {
        impact: "Task deleted as all activities in task have been deleted or marked as already executed.",
        mostRecent: true,
        number: "EV0027",
        timestamp: "2021-11-25T01:39:00.00000000"
    }),
    (event28:Event:System:DeleteMilestone {
        impact: "Milestone deleted as all linked activities to the milestone have been deleted.",
        mostRecent: true,
        number: "EV0028",
        timestamp: "2021-11-25T01:39:00.00000000"
    }),
    (event29:Event:System:Recommendation {
        impact: "The user has selected to use clay models for the project, but has indicated the styling team has a high skill level. SDP recommends reviewing using Virtual Models instead of clay for a cost and timing saving.",
        mostRecent: true,
        number: "EV0029",
        timestamp: "2021-11-25T01:39:00.00000000"
    })


// Connect the events to their relevant node.
CREATE
    (activity17)-[:HAS_EVENT {number: "HEV0001"}]->(event1),
    (activity18)-[:HAS_EVENT {number: "HEV0002"}]->(event2),
    (activity18)-[:HAS_EVENT {number: "HEV0003"}]->(event3),
    (activity19)-[:HAS_EVENT {number: "HEV0004"}]->(event4),
    (activity19)-[:HAS_EVENT {number: "HEV0005"}]->(event5),
    (activity20)-[:HAS_EVENT {number: "HEV0006"}]->(event6),
    (activity20)-[:HAS_EVENT {number: "HEV0007"}]->(event7),
    (activity21)-[:HAS_EVENT {number: "HEV0008"}]->(event8),
    (activity21)-[:HAS_EVENT {number: "HEV0009"}]->(event9),
    (activity22)-[:HAS_EVENT {number: "HEV0010"}]->(event10),
    (activity23)-[:HAS_EVENT {number: "HEV0011"}]->(event11),
    (activity24)-[:HAS_EVENT {number: "HEV0012"}]->(event12),
    (activity24)-[:HAS_EVENT {number: "HEV0013"}]->(event13),
    (activity25)-[:HAS_EVENT {number: "HEV0014"}]->(event14),
    (activity26)-[:HAS_EVENT {number: "HEV0015"}]->(event15),
    (milestone2)-[:HAS_EVENT {number: "HEV0016"}]->(event16),
    (milestone3)-[:HAS_EVENT {number: "HEV0017"}]->(event17),
    (activity13)-[:HAS_EVENT {number: "HEV0018"}]->(event18),
    (activity13)-[:HAS_EVENT {number: "HEV0019"}]->(event19),
    (property4)-[:HAS_EVENT {number: "HEV0020"}]->(event20),
    (swimlane3)-[:HAS_EVENT {number: "HEV0021"}]->(event21),
    (swimlane3)-[:HAS_EVENT {number: "HEV0022"}]->(event22),
    (tag9)-[:HAS_EVENT {number: "HEV0023"}]->(event23),
    (tag9)-[:HAS_EVENT {number: "HEV0024"}]->(event24),
    (specialactivity1)-[:HAS_EVENT {number: "HEV0025"}]->(event25),
    (swimlane2)-[:HAS_EVENT {number: "HEV0026"}]->(event26),
    (task6)-[:HAS_EVENT {number: "HEV0027"}]->(event27),
    (milestone1)-[:HAS_EVENT {number: "HEV0028"}]->(event28)


// Create the edges to connect up the events to their corresponding top node.
CREATE
    (event1)-[:INPUT]->(StartDelayEventTopNode),
    (event2)-[:INPUT]->(DurationChangedWithoutWorkloadChangeEventTopNode),
    (event3)-[:INPUT]->(DurationChangedWithoutWorkloadChangeEventTopNode),
    (event4)-[:INPUT]->(DurationChangedWithoutWorkloadChangeEventTopNode),
    (event5)-[:INPUT]->(DurationChangedWithoutWorkloadChangeEventTopNode),
    (event6)-[:INPUT]->(DurationChangedWithWorkloadChangeEventTopNode),
    (event7)-[:INPUT]->(DurationChangedWithWorkloadChangeEventTopNode),
    (event8)-[:INPUT]->(DurationChangedWithWorkloadChangeEventTopNode),
    (event9)-[:INPUT]->(DurationChangedWithWorkloadChangeEventTopNode),
    (event10)-[:INPUT]->(ResourcesChangedWithoutWorkloadChangeEventTopNode),
    (event11)-[:INPUT]->(ResourcesChangedWithWorkloadChangeEventTopNode),
    (event12)-[:INPUT]->(MaturityChangeEventTopNode),
    (event13)-[:INPUT]->(MaturityChangeEventTopNode),
    (event14)-[:INPUT]->(MaturityChangeEventTopNode),
    (event15)-[:INPUT]->(DeleteActivityEventTopNode),
    (event16)-[:INPUT]->(DeleteMilestoneEventTopNode),
    (event17)-[:INPUT]->(DeleteMilestoneEventTopNode),
    (event18)-[:INPUT]->(MarkAlreadyExecutedEventTopNode),
    (event19)-[:INPUT]->(MarkAlreadyExecutedEventTopNode),
    (event20)-[:INPUT]->(ChangePropertyValueEventTopNode),
    (event21)-[:INPUT]->(DeleteSwimlaneEventTopNode),
    (event22)-[:INPUT]->(HideSwimlaneEventTopNode),
    (event23)-[:INPUT]->(DeleteTagEventTopNode),
    (event24)-[:INPUT]->(HideTagEventTopNode),
    (event25)-[:INPUT]->(SpecialActivityDisappearedSystemTopNode),
    (event26)-[:INPUT]->(DeleteSwimlaneSystemTopNode),
    (event27)-[:INPUT]->(DeleteTaskSystemTopNode),
    (event28)-[:INPUT]->(DeleteMilestoneSystemTopNode),
    (event29)-[:INPUT]->(RecommendationSystemTopNode)