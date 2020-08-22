package common.battle.attack

import common.battle.StageBasis
import common.battle.entity.Entity
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.util.BattleObj
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.UnitLevel
import io.BCPlayer
import page.JL
import page.anim.AnimBox
import page.support.ListJtfPolicy
import page.support.SortTable
import page.view.ViewBox
import page.view.ViewBox.Conf
import page.view.ViewBox.Controller
import page.view.ViewBox.VBExporter

abstract class AtkModelAb(bas: StageBasis) : BattleObj() {
    val b: StageBasis

    /** get the ability bitmask of this attack  */
    abstract fun getAbi(): Int

    /** get the direction of the entity  */
    abstract fun getDire(): Int

    /** get the position of the entity  */
    abstract fun getPos(): Double

    /** invoke when damage calculation is finished  */
    open fun invokeLater(atk: AttackAb, e: Entity) {}
    open fun getLayer(): Int {
        return 10
    }

    init {
        b = bas
    }
}