package common.util.anim

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
import common.system.VImg
import common.system.fake.FakeImage
import common.util.anim.AnimU.ImageKeeper
import common.util.anim.AnimU.UType
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

abstract class AnimU<T : ImageKeeper?> : AnimD<AnimU<*>?, UType> {
    interface ImageKeeper {
        fun getEdi(): VImg?
        fun getIC(): ImgCut
        fun getMA(): Array<MaAnim?>
        fun getMM(): MaModel
        fun getNum(): FakeImage?
        fun getUni(): VImg?
        fun unload()
    }

    enum class UType : AnimType<AnimU<*>?, UType?> {
        WALK, IDLE, ATK, HB, ENTER, BURROW_DOWN, BURROW_MOVE, BURROW_UP
    }

    protected var partial = false
    val loader: T

    protected constructor(path: String, load: T) : super(path) {
        loader = load
    }

    protected constructor(load: T) : super("") {
        loader = load
    }

    fun getAtkLen(): Int {
        partial()
        return anims.get(2).len + 1
    }

    override fun getEAnim(t: UType): EAnimU? {
        check()
        return EAnimU(this, t)
    }

    fun getEdi(): VImg {
        return loader.getEdi()
    }

    override fun getNum(): FakeImage? {
        return loader.getNum()
    }

    fun getUni(): VImg {
        return loader.getUni()
    }

    override fun load() {
        loaded = true
        try {
            imgcut = loader.getIC()
            if (getNum() == null) {
                mamodel = null
                return
            }
            parts = imgcut!!.cut(getNum())
            partial()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun unload() {
        loader.unload()
        super.unload()
    }

    protected open fun partial() {
        if (!partial) {
            partial = true
            imgcut = loader.getIC()
            mamodel = loader.getMM()
            anims = loader.getMA()
            types = if (anims.size == 4) TYPE4 else if (anims.size == 5) TYPE5 else TYPE7
        }
    }

    companion object {
        val TYPE4 = arrayOf(UType.WALK, UType.IDLE, UType.ATK, UType.HB)
        val TYPE5 = arrayOf(UType.WALK, UType.IDLE, UType.ATK, UType.HB, UType.ENTER)
        val TYPE7 = arrayOf(UType.WALK, UType.IDLE, UType.ATK, UType.HB, UType.BURROW_DOWN,
                UType.BURROW_MOVE, UType.BURROW_UP)
    }
}